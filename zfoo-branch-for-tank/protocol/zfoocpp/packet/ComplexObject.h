#ifndef ZFOO_ComplexObject
#define ZFOO_ComplexObject

#include "zfoocpp/ByteBuffer.h"
#include "zfoocpp/Packet/ObjectA.h"
#include "zfoocpp/Packet/ObjectB.h"
namespace zfoo {
    // 复杂的对象，包括了各种复杂的结构，数组，List，Set，Map
    class ComplexObject : public IProtocol {
    public:
        // byte类型，最简单的整形
        int8_t a;
        // byte的包装类型，优先使用基础类型，包装类型会有装箱拆箱
        int8_t aa;
        // 数组类型
        vector<int8_t> aaa;
        vector<int8_t> aaaa;
        int16_t b;
        int16_t bb;
        vector<int16_t> bbb;
        vector<int16_t> bbbb;
        int32_t c;
        int32_t cc;
        vector<int32_t> ccc;
        vector<int32_t> cccc;
        int64_t d;
        int64_t dd;
        vector<int64_t> ddd;
        vector<int64_t> dddd;
        float e;
        float ee;
        vector<float> eee;
        vector<float> eeee;
        double f;
        double ff;
        vector<double> fff;
        vector<double> ffff;
        bool g;
        bool gg;
        vector<bool> ggg;
        vector<bool> gggg;
        string jj;
        vector<string> jjj;
        ObjectA kk;
        vector<ObjectA> kkk;
        list<int32_t> l;
        list<list<list<int32_t>>> ll;
        list<list<ObjectA>> lll;
        list<string> llll;
        list<map<int32_t, string>> lllll;
        map<int32_t, string> m;
        map<int32_t, ObjectA> mm;
        map<ObjectA, list<int32_t>> mmm;
        map<list<list<ObjectA>>, list<list<list<int32_t>>>> mmmm;
        map<list<map<int32_t, string>>, set<map<int32_t, string>>> mmmmm;
        set<int32_t> s;
        set<set<list<int32_t>>> ss;
        set<set<ObjectA>> sss;
        set<string> ssss;
        set<map<int32_t, string>> sssss;
        // 如果要修改协议并且兼容老协议，需要加上Compatible注解，保持Compatible注解的value自增
        int32_t myCompatible;
        ObjectA myObject;
    
        ~ComplexObject() override = default;
    
        int16_t protocolId() override {
            return 100;
        }
    };

    class ComplexObjectRegistration : public IProtocolRegistration {
    public:
        int16_t protocolId() override {
            return 100;
        }
    
        void write(ByteBuffer &buffer, IProtocol *packet) override {
            if (packet == nullptr) {
                buffer.writeInt(0);
                return;
            }
            auto *message = (ComplexObject *) packet;
            auto beforeWriteIndex = buffer.writerIndex();
            buffer.writeInt(36962);
            buffer.writeByte(message->a);
            buffer.writeByte(message->aa);
            buffer.writeByteArray(message->aaa);
            buffer.writeByteArray(message->aaaa);
            buffer.writeShort(message->b);
            buffer.writeShort(message->bb);
            buffer.writeShortArray(message->bbb);
            buffer.writeShortArray(message->bbbb);
            buffer.writeInt(message->c);
            buffer.writeInt(message->cc);
            buffer.writeIntArray(message->ccc);
            buffer.writeIntArray(message->cccc);
            buffer.writeLong(message->d);
            buffer.writeLong(message->dd);
            buffer.writeLongArray(message->ddd);
            buffer.writeLongArray(message->dddd);
            buffer.writeFloat(message->e);
            buffer.writeFloat(message->ee);
            buffer.writeFloatArray(message->eee);
            buffer.writeFloatArray(message->eeee);
            buffer.writeDouble(message->f);
            buffer.writeDouble(message->ff);
            buffer.writeDoubleArray(message->fff);
            buffer.writeDoubleArray(message->ffff);
            buffer.writeBool(message->g);
            buffer.writeBool(message->gg);
            buffer.writeBooleanArray(message->ggg);
            buffer.writeBooleanArray(message->gggg);
            buffer.writeString(message->jj);
            buffer.writeStringArray(message->jjj);
            buffer.writePacket(&message->kk, 102);
            buffer.writePacketArray<ObjectA>(message->kkk, 102);
            buffer.writeIntList(message->l);
            buffer.writeInt(message->ll.size());
            for (auto i0 : message->ll) {
                buffer.writeInt(i0.size());
                for (auto i1 : i0) {
                    buffer.writeIntList(i1);
                }
            }
            buffer.writeInt(message->lll.size());
            for (auto i2 : message->lll) {
                buffer.writePacketList(i2, 102);
            }
            buffer.writeStringList(message->llll);
            buffer.writeInt(message->lllll.size());
            for (auto i3 : message->lllll) {
                buffer.writeIntStringMap(i3);
            }
            buffer.writeIntStringMap(message->m);
            buffer.writeIntPacketMap(message->mm, 102);
            buffer.writeInt(message->mmm.size());
            for (auto&[keyElement4, valueElement5] : message->mmm) {
                buffer.writePacket((IProtocol *) &keyElement4, 102);
                buffer.writeIntList(valueElement5);
            }
            buffer.writeInt(message->mmmm.size());
            for (auto&[keyElement6, valueElement7] : message->mmmm) {
                buffer.writeInt(keyElement6.size());
                for (auto i8 : keyElement6) {
                    buffer.writePacketList(i8, 102);
                }
                buffer.writeInt(valueElement7.size());
                for (auto i9 : valueElement7) {
                    buffer.writeInt(i9.size());
                    for (auto i10 : i9) {
                        buffer.writeIntList(i10);
                    }
                }
            }
            buffer.writeInt(message->mmmmm.size());
            for (auto&[keyElement11, valueElement12] : message->mmmmm) {
                buffer.writeInt(keyElement11.size());
                for (auto i13 : keyElement11) {
                    buffer.writeIntStringMap(i13);
                }
                buffer.writeInt(valueElement12.size());
                for (auto i14 : valueElement12) {
                    buffer.writeIntStringMap(i14);
                }
            }
            buffer.writeIntSet(message->s);
            buffer.writeInt(message->ss.size());
            for (auto i15 : message->ss) {
                buffer.writeInt(i15.size());
                for (auto i16 : i15) {
                    buffer.writeIntList(i16);
                }
            }
            buffer.writeInt(message->sss.size());
            for (auto i17 : message->sss) {
                buffer.writePacketSet(i17, 102);
            }
            buffer.writeStringSet(message->ssss);
            buffer.writeInt(message->sssss.size());
            for (auto i18 : message->sssss) {
                buffer.writeIntStringMap(i18);
            }
            buffer.writeInt(message->myCompatible);
            buffer.writePacket(&message->myObject, 102);
            buffer.adjustPadding(36962, beforeWriteIndex);
        }
    
        IProtocol *read(ByteBuffer &buffer) override {
            auto *packet = new ComplexObject();
            auto length = buffer.readInt();
            if (length == 0) {
                return packet;
            }
            auto beforeReadIndex = buffer.readerIndex();
            int8_t result0 = buffer.readByte();
            packet->a = result0;
            int8_t result1 = buffer.readByte();
            packet->aa = result1;
            auto array2 = buffer.readByteArray();
            packet->aaa = array2;
            auto array3 = buffer.readByteArray();
            packet->aaaa = array3;
            auto result4 = buffer.readShort();
            packet->b = result4;
            auto result5 = buffer.readShort();
            packet->bb = result5;
            auto array6 = buffer.readShortArray();
            packet->bbb = array6;
            auto array7 = buffer.readShortArray();
            packet->bbbb = array7;
            int32_t result8 = buffer.readInt();
            packet->c = result8;
            int32_t result9 = buffer.readInt();
            packet->cc = result9;
            auto array10 = buffer.readIntArray();
            packet->ccc = array10;
            auto array11 = buffer.readIntArray();
            packet->cccc = array11;
            auto result12 = buffer.readLong();
            packet->d = result12;
            auto result13 = buffer.readLong();
            packet->dd = result13;
            auto array14 = buffer.readLongArray();
            packet->ddd = array14;
            auto array15 = buffer.readLongArray();
            packet->dddd = array15;
            float result16 = buffer.readFloat();
            packet->e = result16;
            float result17 = buffer.readFloat();
            packet->ee = result17;
            auto array18 = buffer.readFloatArray();
            packet->eee = array18;
            auto array19 = buffer.readFloatArray();
            packet->eeee = array19;
            double result20 = buffer.readDouble();
            packet->f = result20;
            double result21 = buffer.readDouble();
            packet->ff = result21;
            auto array22 = buffer.readDoubleArray();
            packet->fff = array22;
            auto array23 = buffer.readDoubleArray();
            packet->ffff = array23;
            bool result24 = buffer.readBool();
            packet->g = result24;
            bool result25 = buffer.readBool();
            packet->gg = result25;
            auto array26 = buffer.readBooleanArray();
            packet->ggg = array26;
            auto array27 = buffer.readBooleanArray();
            packet->gggg = array27;
            auto result28 = buffer.readString();
            packet->jj = result28;
            auto array29 = buffer.readStringArray();
            packet->jjj = array29;
            auto result30 = buffer.readPacket(102);
            auto *result31 = (ObjectA *) result30.get();
            packet->kk = *result31;
            auto array32 = buffer.readPacketArray<ObjectA>(102);
            packet->kkk = array32;
            auto list33 = buffer.readIntList();
            packet->l = list33;
            int32_t size36 = buffer.readInt();
            list<list<list<int32_t>>> result34;
            for (int index35 = 0; index35 < size36; index35++) {
                int32_t size39 = buffer.readInt();
                list<list<int32_t>> result37;
                for (int index38 = 0; index38 < size39; index38++) {
                    auto list40 = buffer.readIntList();
                    result37.emplace_back(list40);
                }
                result34.emplace_back(result37);
            }
            packet->ll = result34;
            int32_t size43 = buffer.readInt();
            list<list<ObjectA>> result41;
            for (int index42 = 0; index42 < size43; index42++) {
                auto list44 = buffer.readPacketList<ObjectA>(102);
                result41.emplace_back(list44);
            }
            packet->lll = result41;
            auto list45 = buffer.readStringList();
            packet->llll = list45;
            int32_t size48 = buffer.readInt();
            list<map<int32_t, string>> result46;
            for (int index47 = 0; index47 < size48; index47++) {
                auto map49 = buffer.readIntStringMap();
                result46.emplace_back(map49);
            }
            packet->lllll = result46;
            auto map50 = buffer.readIntStringMap();
            packet->m = map50;
            auto map51 = buffer.readIntPacketMap<ObjectA>(102);
            packet->mm = map51;
            int32_t size53 = buffer.readInt();
            map<ObjectA, list<int32_t>> result52;
            for (auto index54 = 0; index54 < size53; index54++) {
                auto result55 = buffer.readPacket(102);
                auto *result56 = (ObjectA *) result55.get();
                auto list57 = buffer.readIntList();
                result52.insert(make_pair(*result56, list57));
            }
            packet->mmm = result52;
            int32_t size59 = buffer.readInt();
            map<list<list<ObjectA>>, list<list<list<int32_t>>>> result58;
            for (auto index60 = 0; index60 < size59; index60++) {
                int32_t size63 = buffer.readInt();
                list<list<ObjectA>> result61;
                for (int index62 = 0; index62 < size63; index62++) {
                    auto list64 = buffer.readPacketList<ObjectA>(102);
                    result61.emplace_back(list64);
                }
                int32_t size67 = buffer.readInt();
                list<list<list<int32_t>>> result65;
                for (int index66 = 0; index66 < size67; index66++) {
                    int32_t size70 = buffer.readInt();
                    list<list<int32_t>> result68;
                    for (int index69 = 0; index69 < size70; index69++) {
                        auto list71 = buffer.readIntList();
                        result68.emplace_back(list71);
                    }
                    result65.emplace_back(result68);
                }
                result58.insert(make_pair(result61, result65));
            }
            packet->mmmm = result58;
            int32_t size73 = buffer.readInt();
            map<list<map<int32_t, string>>, set<map<int32_t, string>>> result72;
            for (auto index74 = 0; index74 < size73; index74++) {
                int32_t size77 = buffer.readInt();
                list<map<int32_t, string>> result75;
                for (int index76 = 0; index76 < size77; index76++) {
                    auto map78 = buffer.readIntStringMap();
                    result75.emplace_back(map78);
                }
                int32_t size81 = buffer.readInt();
                set<map<int32_t, string>> result79;
                for (int index80 = 0; index80 < size81; index80++) {
                    auto map82 = buffer.readIntStringMap();
                    result79.emplace(map82);
                }
                result72.insert(make_pair(result75, result79));
            }
            packet->mmmmm = result72;
            auto set83 = buffer.readIntSet();
            packet->s = set83;
            int32_t size86 = buffer.readInt();
            set<set<list<int32_t>>> result84;
            for (int index85 = 0; index85 < size86; index85++) {
                int32_t size89 = buffer.readInt();
                set<list<int32_t>> result87;
                for (int index88 = 0; index88 < size89; index88++) {
                    auto list90 = buffer.readIntList();
                    result87.emplace(list90);
                }
                result84.emplace(result87);
            }
            packet->ss = result84;
            int32_t size93 = buffer.readInt();
            set<set<ObjectA>> result91;
            for (int index92 = 0; index92 < size93; index92++) {
                auto set94 = buffer.readPacketSet<ObjectA>(102);
                result91.emplace(set94);
            }
            packet->sss = result91;
            auto set95 = buffer.readStringSet();
            packet->ssss = set95;
            int32_t size98 = buffer.readInt();
            set<map<int32_t, string>> result96;
            for (int index97 = 0; index97 < size98; index97++) {
                auto map99 = buffer.readIntStringMap();
                result96.emplace(map99);
            }
            packet->sssss = result96;
            if (buffer.compatibleRead(beforeReadIndex, length)) {
                int32_t result100 = buffer.readInt();
                packet->myCompatible = result100;
            }
            if (buffer.compatibleRead(beforeReadIndex, length)) {
                auto result101 = buffer.readPacket(102);
                auto *result102 = (ObjectA *) result101.get();
                packet->myObject = *result102;
            }
            if (length > 0) {
                buffer.readerIndex(beforeReadIndex + length);
            }
            return packet;
        }
    };
}

#endif