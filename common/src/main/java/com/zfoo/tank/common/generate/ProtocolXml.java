package com.zfoo.tank.common.generate;

import com.zfoo.net.packet.IPacket;
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
public class ProtocolXml {
    
    
    private static final String SUFFIX_PATTERN = "**/*.class";
    
    private static String protocolOutputRootPath = "src/main/resources/";
    private static String protocolName = "protocol";
    
    public static String xml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
    }
    
    public static String notes() {
        return "<!-- native为内部消息，common是公共消息每个模块都能使用 -->";
    }
    
    public static String beginProtocols() {
        return "<protocols>";
    }
    
    public static String endProtocols() {
        return "</protocols>";
    }
    
    public static String beginModule(ProtoModule module) {
        return StringUtils.format("<module id={}{}{} name={}{}{}>", StringUtils.QUOTATION_MARK, module.id, StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK, module.name, StringUtils.QUOTATION_MARK
                , StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK);
    }
    
    public static String endModule() {
        return "</module>";
    }
    
    public static String protocol(Short id, String clazz) {
        return StringUtils.format("<protocol id={}{}{} location={}{}{}/>", StringUtils.QUOTATION_MARK, id, StringUtils.QUOTATION_MARK, StringUtils.QUOTATION_MARK, clazz, StringUtils.QUOTATION_MARK);
    }
    
    public static void createProtocolXML() {
        var builder = new StringBuilder();
        builder.append(xml());
        builder.append(LS).append(LS);
        builder.append(notes()).append(LS);
        builder.append(beginProtocols()).append(LS);
        builder.append(LS);
        var modules = ProtoModule.values();
        for (var module : modules) {
            builder.append(TAB).append(beginModule(module));
            for (String packageName : module.modules) {
                var map = scanPacketClazz(packageName);
                for (var entry : map.entrySet()) {
                    builder.append(LS);
                    builder.append(TAB).append(TAB);
                    builder.append(protocol(entry.getKey(), entry.getValue().getName()));
                }
                builder.append(LS);
            }
            builder.append(TAB).append(endModule()).append(LS);
            builder.append(LS);
        }
        
        builder.append(endProtocols()).append(LS);

        var protocolOutputPath = StringUtils.format("{}/{}.xml", protocolOutputRootPath, protocolName);
        FileUtils.writeStringToFile(new File(protocolOutputPath), builder.toString(), false);
    }

    private static Map<Short, Class<?>> scanPacketClazz(String packageName) {
        var clazzMap = new TreeMap<Short, Class<?>>();
        var resourcePatternResolver = new PathMatchingResourcePatternResolver();
        var metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        try {
            var packageSearchPath = ResourceUtils.CLASSPATH_URL_PREFIX + packageName.replace(StringUtils.PERIOD, StringUtils.SLASH) + StringUtils.SLASH + SUFFIX_PATTERN;
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
                    if (!packageName.equals(clazz.getPackageName())) {
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
}
