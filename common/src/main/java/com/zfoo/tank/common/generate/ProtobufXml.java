package com.zfoo.tank.common.generate;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.registration.ProtocolAnalysis;
import com.zfoo.protocol.util.FileUtils;
import com.zfoo.protocol.util.StringUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import static com.zfoo.protocol.util.FileUtils.LS;
import static com.zfoo.protocol.util.StringUtils.TAB;

/**
 * 协议号配置
 * 协议从1000开始，默认每个模块占用100个协议号
 * 最大配置id为32000 共用结构体协议id定义在自己模块id范围
 * @author SHICHANG11
 *
 */
public class ProtobufXml {
    
    
    private static final String SUFFIX_PATTERN = "**/*.class";
    
    private static String protocolOutputRootPath = "src/main/resources/";
    private static String protocolName = "protobuf";
    
    public static String xml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
    }
    
    public static String beginProtobuf() {
        return "<protobuf syntax=\"proto3\">";
    }
    
    public static String endProtobuf() {
        return "</protobuf>";
    }

    public static String beginProto(String name, String importStr, String option) {
        return StringUtils.format("<proto name={}{}{} import={}{}{} option={}{}{}>", StringUtils.QUOTATION_MARK, name, StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK, importStr, StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK, option, StringUtils.QUOTATION_MARK);
    }
    
    public static String endProto() {
        return "</proto>";
    }
    
    public static String protocol(String clazzName) {
        return StringUtils.format("<protocol location={}{}{}/>", StringUtils.QUOTATION_MARK, clazzName, StringUtils.QUOTATION_MARK);
    }
    
    public static void createProtobufXML() {
        var clazzMap = createProtocols();
        var builder = new StringBuilder();
        builder.append(xml());
        builder.append(LS).append(LS);
        builder.append(beginProtobuf()).append(LS);
        builder.append(LS);
        for (var map : clazzMap.entrySet()) {
            var name = map.getKey();
            String importPack = "common";
            if (name.equals(importPack)) {
                importPack = "";
            }
            var protoMap = map.getValue();
            var hasProto = protoMap.values().stream().filter(k -> !k.endsWith("Ask") && !k.endsWith("Answer")).findAny().isPresent();
            if (!hasProto) {
                continue;
            }
            builder.append(TAB).append(beginProto(name, importPack, ""));
            for (var calzzMap : protoMap.entrySet()) {
                //过滤内部消息
                var clazzName = calzzMap.getValue();
                if(clazzName.endsWith("Ask") || clazzName.endsWith("Answer")) {
                    continue;
                }
                builder.append(LS);
                builder.append(TAB).append(TAB);
                builder.append(protocol(calzzMap.getValue()));
            }
            builder.append(LS);
            builder.append(TAB).append(endProto()).append(LS);
            builder.append(LS);
        }
        
        builder.append(endProtobuf()).append(LS);

        var protocolOutputPath = StringUtils.format("{}/{}.xml", protocolOutputRootPath, protocolName);
        FileUtils.writeStringToFile(new File(protocolOutputPath), builder.toString(), false);
    }
    
    public static Map<String, Map<Short, String>> createProtocols() {
        //生成公共模块和结构体
        var allGenerateProtocols = new TreeMap<Short, Class<?>>();
        for (var module : ProtoModule.values()) {
            if (module.id < ProtoModule.COMMON.id) {
                continue;
            }
            for (String packageName : module.modules) {
                var scanPacketMap = scanPacketClazz(packageName);
                allGenerateProtocols.putAll(scanPacketMap);
            }
        }
        //协议分模块
        var protocolsMap = new TreeMap<String, Map<Short, String>>();
        for (var map : allGenerateProtocols.entrySet()) {
            var clazzName = map.getValue();
            String moduleName = StringUtils.substringAfterLast(clazzName.getPackageName(), StringUtils.PERIOD);
            var subMap = protocolsMap.computeIfAbsent(moduleName, k -> new TreeMap<>());
            subMap.put(map.getKey(), clazzName.getName());
        }
        return protocolsMap;
    }

    private static Map<Short, Class<?>> scanPacketClazz(String scanLocation) {
        var clazzMap = new TreeMap<Short, Class<?>>();
        var resourcePatternResolver = new PathMatchingResourcePatternResolver();
        var metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        try {
            var packageSearchPath = ResourceUtils.CLASSPATH_URL_PREFIX + scanLocation.replace(StringUtils.PERIOD, StringUtils.SLASH) + StringUtils.SLASH + SUFFIX_PATTERN;
            var resources = resourcePatternResolver.getResources(packageSearchPath);
            var result = new HashSet<String>();
            var packetClazz = IPacket.class;
            for (var resource : resources) {
                if (resource.isReadable()) {
                    var metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    var clazzMeta = metadataReader.getClassMetadata();
                    if (clazzMeta.isInterface() || clazzMeta.isAbstract()) {
                        continue;
                    }
                    Class<?> clazz = Class.forName(clazzMeta.getClassName());
                    var isProtobuf = checkProtobufProtocol(clazz);
                    if (!isProtobuf) {
                        continue;
                    }
                    if (packetClazz.isAssignableFrom(clazz)) {
                        result.add(clazzMeta.getClassName());
                        short protoId = ProtocolAnalysis.getProtocolIdAndCheckClass(clazz);
                        clazzMap.put(protoId, clazz);
                    }
                }
            }
            return clazzMap;
        } catch (Exception e) {
            throw new RuntimeException("无法读取资源信息:" + e);
        }
    }
    
    /**
     * 检测是否是protobuf协议
     * @param clazz
     * @return
     */
    public static boolean checkProtobufProtocol(Class<?> clazz) {
        var protobufClass = clazz.getDeclaredAnnotation(ProtobufClass.class);
        if (protobufClass != null) {
            return true;
        }
        var fieldArr = clazz.getDeclaredFields();
        for (var field : fieldArr) {
            var protobuf = field.getDeclaredAnnotation(Protobuf.class);
            if (protobuf != null) {
                return true;
            }
        }
        return false;
    }
}
