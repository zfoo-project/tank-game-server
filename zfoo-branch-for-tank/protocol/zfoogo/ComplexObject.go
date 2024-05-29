package protocol
// 复杂的对象，包括了各种复杂的结构，数组，List，Set，Map
type ComplexObject struct {
	A int8
    Aa int8
    Aaa []int8
    Aaaa []int8
    B int16
    Bb int16
    Bbb []int16
    Bbbb []int16
    C int
    Cc int
    Ccc []int
    Cccc []int
    D int64
    Dd int64
    Ddd []int64
    Dddd []int64
    E float32
    Ee float32
    Eee []float32
    Eeee []float32
    F float64
    Ff float64
    Fff []float64
    Ffff []float64
    G bool
    Gg bool
    Ggg []bool
    Gggg []bool
    Jj string
    Jjj []string
    Kk ObjectA
    Kkk []ObjectA
    L []int
    Ll [][][]int
    Lll [][]ObjectA
    Llll []string
    Lllll []map[int]string
    M map[int]string
    Mm map[int]ObjectA
    Mmm map[ObjectA][]int
    Mmmm map[[][]ObjectA][][][]int
    Mmmmm map[[]map[int]string][]map[int]string
    S []int
    Ss [][][]int
    Sss [][]ObjectA
    Ssss []string
    Sssss []map[int]string
    MyCompatible int
    MyObject ObjectA
}

func (protocol ComplexObject) ProtocolId() int16 {
	return 100
}

func (protocol ComplexObject) write(buffer *ByteBuffer, packet any) {
	if packet == nil {
	    buffer.WriteInt(0)
		return
	}
	var message = packet.(*ComplexObject)
	var beforeWriteIndex = buffer.WriteOffset()
    buffer.WriteInt(36962)
    buffer.WriteByte(message.A)
    buffer.WriteByte(message.Aa)
    buffer.WriteByteArray(message.Aaa)
    buffer.WriteByteArray(message.Aaaa)
    buffer.WriteShort(message.B)
    buffer.WriteShort(message.Bb)
    buffer.WriteShortArray(message.Bbb)
    buffer.WriteShortArray(message.Bbbb)
    buffer.WriteInt(message.C)
    buffer.WriteInt(message.Cc)
    buffer.WriteIntArray(message.Ccc)
    buffer.WriteIntArray(message.Cccc)
    buffer.WriteLong(message.D)
    buffer.WriteLong(message.Dd)
    buffer.WriteLongArray(message.Ddd)
    buffer.WriteLongArray(message.Dddd)
    buffer.WriteFloat(message.E)
    buffer.WriteFloat(message.Ee)
    buffer.WriteFloatArray(message.Eee)
    buffer.WriteFloatArray(message.Eeee)
    buffer.WriteDouble(message.F)
    buffer.WriteDouble(message.Ff)
    buffer.WriteDoubleArray(message.Fff)
    buffer.WriteDoubleArray(message.Ffff)
    buffer.WriteBool(message.G)
    buffer.WriteBool(message.Gg)
    buffer.WriteBooleanArray(message.Ggg)
    buffer.WriteBooleanArray(message.Gggg)
    buffer.WriteString(message.Jj)
    buffer.WriteStringArray(message.Jjj)
    buffer.WritePacket(&message.Kk, 102)
    if (message.Kkk == nil) || (len(message.Kkk) == 0) {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Kkk))
        var length0 = len(message.Kkk)
        for i1 := 0; i1 < length0; i1++ {
            var element2 = message.Kkk[i1]
            buffer.WritePacket(&element2, 102)
        }
    }
    buffer.WriteIntArray(message.L)
    if message.Ll == nil {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Ll))
        var length3 = len(message.Ll)
        for i4 := 0; i4 < length3; i4++ {
            var element5 = message.Ll[i4]
            if element5 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(element5))
                var length6 = len(element5)
                for i7 := 0; i7 < length6; i7++ {
                    var element8 = element5[i7]
                    buffer.WriteIntArray(element8)
                }
            }
        }
    }
    if message.Lll == nil {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Lll))
        var length9 = len(message.Lll)
        for i10 := 0; i10 < length9; i10++ {
            var element11 = message.Lll[i10]
            if element11 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(element11))
                var length12 = len(element11)
                for i13 := 0; i13 < length12; i13++ {
                    var element14 = element11[i13]
                    buffer.WritePacket(&element14, 102)
                }
            }
        }
    }
    buffer.WriteStringArray(message.Llll)
    if message.Lllll == nil {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Lllll))
        var length15 = len(message.Lllll)
        for i16 := 0; i16 < length15; i16++ {
            var element17 = message.Lllll[i16]
            buffer.WriteIntStringMap(element17)
        }
    }
    buffer.WriteIntStringMap(message.M)
    if (message.Mm == nil) || (len(message.Mm) == 0) {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Mm))
        for keyElement19, valueElement20 := range message.Mm {
            buffer.WriteInt(keyElement19)
            buffer.WritePacket(&valueElement20, 102)
        }
    }
    if (message.Mmm == nil) || (len(message.Mmm) == 0) {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Mmm))
        for keyElement22, valueElement23 := range message.Mmm {
            buffer.WritePacket(&keyElement22, 102)
            buffer.WriteIntArray(valueElement23)
        }
    }
    if (message.Mmmm == nil) || (len(message.Mmmm) == 0) {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Mmmm))
        for keyElement25, valueElement26 := range message.Mmmm {
            if keyElement25 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(keyElement25))
                var length27 = len(keyElement25)
                for i28 := 0; i28 < length27; i28++ {
                    var element29 = keyElement25[i28]
                    if element29 == nil {
                        buffer.WriteInt(0)
                    } else {
                        buffer.WriteInt(len(element29))
                        var length30 = len(element29)
                        for i31 := 0; i31 < length30; i31++ {
                            var element32 = element29[i31]
                            buffer.WritePacket(&element32, 102)
                        }
                    }
                }
            }
            if valueElement26 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(valueElement26))
                var length33 = len(valueElement26)
                for i34 := 0; i34 < length33; i34++ {
                    var element35 = valueElement26[i34]
                    if element35 == nil {
                        buffer.WriteInt(0)
                    } else {
                        buffer.WriteInt(len(element35))
                        var length36 = len(element35)
                        for i37 := 0; i37 < length36; i37++ {
                            var element38 = element35[i37]
                            buffer.WriteIntArray(element38)
                        }
                    }
                }
            }
        }
    }
    if (message.Mmmmm == nil) || (len(message.Mmmmm) == 0) {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Mmmmm))
        for keyElement40, valueElement41 := range message.Mmmmm {
            if keyElement40 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(keyElement40))
                var length42 = len(keyElement40)
                for i43 := 0; i43 < length42; i43++ {
                    var element44 = keyElement40[i43]
                    buffer.WriteIntStringMap(element44)
                }
            }
            if valueElement41 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(valueElement41))
                var length45 = len(valueElement41)
                for i46 := 0; i46 < length45; i46++ {
                    var element47 = valueElement41[i46]
                    buffer.WriteIntStringMap(element47)
                }
            }
        }
    }
    buffer.WriteIntArray(message.S)
    if message.Ss == nil {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Ss))
        var length48 = len(message.Ss)
        for i49 := 0; i49 < length48; i49++ {
            var element50 = message.Ss[i49]
            if element50 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(element50))
                var length51 = len(element50)
                for i52 := 0; i52 < length51; i52++ {
                    var element53 = element50[i52]
                    buffer.WriteIntArray(element53)
                }
            }
        }
    }
    if message.Sss == nil {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Sss))
        var length54 = len(message.Sss)
        for i55 := 0; i55 < length54; i55++ {
            var element56 = message.Sss[i55]
            if element56 == nil {
                buffer.WriteInt(0)
            } else {
                buffer.WriteInt(len(element56))
                var length57 = len(element56)
                for i58 := 0; i58 < length57; i58++ {
                    var element59 = element56[i58]
                    buffer.WritePacket(&element59, 102)
                }
            }
        }
    }
    buffer.WriteStringArray(message.Ssss)
    if message.Sssss == nil {
        buffer.WriteInt(0)
    } else {
        buffer.WriteInt(len(message.Sssss))
        var length60 = len(message.Sssss)
        for i61 := 0; i61 < length60; i61++ {
            var element62 = message.Sssss[i61]
            buffer.WriteIntStringMap(element62)
        }
    }
    buffer.WriteInt(message.MyCompatible)
    buffer.WritePacket(&message.MyObject, 102)
    buffer.AdjustPadding(36962, beforeWriteIndex)
}

func (protocol ComplexObject) read(buffer *ByteBuffer) any {
	var packet = new(ComplexObject)
	var length = buffer.ReadInt()
	if length == 0 {
		return packet
	}
	var beforeReadIndex = buffer.ReadOffset()
	var result0 = buffer.ReadByte()
    packet.A = result0
    var result1 = buffer.ReadByte()
    packet.Aa = result1
    var array2 = buffer.ReadByteArray()
    packet.Aaa = array2
    var array3 = buffer.ReadByteArray()
    packet.Aaaa = array3
    var result4 = buffer.ReadShort()
    packet.B = result4
    var result5 = buffer.ReadShort()
    packet.Bb = result5
    var array6 = buffer.ReadShortArray()
    packet.Bbb = array6
    var array7 = buffer.ReadShortArray()
    packet.Bbbb = array7
    var result8 = buffer.ReadInt()
    packet.C = result8
    var result9 = buffer.ReadInt()
    packet.Cc = result9
    var array10 = buffer.ReadIntArray()
    packet.Ccc = array10
    var array11 = buffer.ReadIntArray()
    packet.Cccc = array11
    var result12 = buffer.ReadLong()
    packet.D = result12
    var result13 = buffer.ReadLong()
    packet.Dd = result13
    var array14 = buffer.ReadLongArray()
    packet.Ddd = array14
    var array15 = buffer.ReadLongArray()
    packet.Dddd = array15
    var result16 = buffer.ReadFloat()
    packet.E = result16
    var result17 = buffer.ReadFloat()
    packet.Ee = result17
    var array18 = buffer.ReadFloatArray()
    packet.Eee = array18
    var array19 = buffer.ReadFloatArray()
    packet.Eeee = array19
    var result20 = buffer.ReadDouble()
    packet.F = result20
    var result21 = buffer.ReadDouble()
    packet.Ff = result21
    var array22 = buffer.ReadDoubleArray()
    packet.Fff = array22
    var array23 = buffer.ReadDoubleArray()
    packet.Ffff = array23
    var result24 = buffer.ReadBool()
    packet.G = result24
    var result25 = buffer.ReadBool()
    packet.Gg = result25
    var array26 = buffer.ReadBooleanArray()
    packet.Ggg = array26
    var array27 = buffer.ReadBooleanArray()
    packet.Gggg = array27
    var result28 = buffer.ReadString()
    packet.Jj = result28
    var array29 = buffer.ReadStringArray()
    packet.Jjj = array29
    var result30 = *buffer.ReadPacket(102).(*ObjectA)
    packet.Kk = result30
    var size33 = buffer.ReadInt()
    var result31 = make([]ObjectA, size33)
    if size33 > 0 {
        for index32 := 0; index32 < size33; index32++ {
            var result34 = *buffer.ReadPacket(102).(*ObjectA)
            result31[index32] = result34
        }
    }
    packet.Kkk = result31
    var list35 = buffer.ReadIntArray()
    packet.L = list35
    var size38 = buffer.ReadInt()
    var result36 = make([][][]int, size38)
    if size38 > 0 {
        for index37 := 0; index37 < size38; index37++ {
            var size41 = buffer.ReadInt()
            var result39 = make([][]int, size41)
            if size41 > 0 {
                for index40 := 0; index40 < size41; index40++ {
                    var list42 = buffer.ReadIntArray()
                    result39[index40] = list42
                }
            }
            result36[index37] = result39
        }
    }
    packet.Ll = result36
    var size45 = buffer.ReadInt()
    var result43 = make([][]ObjectA, size45)
    if size45 > 0 {
        for index44 := 0; index44 < size45; index44++ {
            var size48 = buffer.ReadInt()
            var result46 = make([]ObjectA, size48)
            if size48 > 0 {
                for index47 := 0; index47 < size48; index47++ {
                    var result49 = *buffer.ReadPacket(102).(*ObjectA)
                    result46[index47] = result49
                }
            }
            result43[index44] = result46
        }
    }
    packet.Lll = result43
    var list50 = buffer.ReadStringArray()
    packet.Llll = list50
    var size53 = buffer.ReadInt()
    var result51 = make([]map[int]string, size53)
    if size53 > 0 {
        for index52 := 0; index52 < size53; index52++ {
            var map54 = buffer.ReadIntStringMap()
            result51[index52] = map54
        }
    }
    packet.Lllll = result51
    var map55 = buffer.ReadIntStringMap()
    packet.M = map55
    var size57 = buffer.ReadInt()
    var result56 = make(map[int]ObjectA)
    if size57 > 0 {
        for index58 := 0; index58 < size57; index58++ {
            var result59 = buffer.ReadInt()
            var result60 = *buffer.ReadPacket(102).(*ObjectA)
            result56[result59] = result60
        }
    }
    packet.Mm = result56
    var size62 = buffer.ReadInt()
    var result61 = make(map[ObjectA][]int)
    if size62 > 0 {
        for index63 := 0; index63 < size62; index63++ {
            var result64 = *buffer.ReadPacket(102).(*ObjectA)
            var list65 = buffer.ReadIntArray()
            result61[result64] = list65
        }
    }
    packet.Mmm = result61
    var size67 = buffer.ReadInt()
    var result66 = make(map[[][]ObjectA][][][]int)
    if size67 > 0 {
        for index68 := 0; index68 < size67; index68++ {
            var size71 = buffer.ReadInt()
            var result69 = make([][]ObjectA, size71)
            if size71 > 0 {
                for index70 := 0; index70 < size71; index70++ {
                    var size74 = buffer.ReadInt()
                    var result72 = make([]ObjectA, size74)
                    if size74 > 0 {
                        for index73 := 0; index73 < size74; index73++ {
                            var result75 = *buffer.ReadPacket(102).(*ObjectA)
                            result72[index73] = result75
                        }
                    }
                    result69[index70] = result72
                }
            }
            var size78 = buffer.ReadInt()
            var result76 = make([][][]int, size78)
            if size78 > 0 {
                for index77 := 0; index77 < size78; index77++ {
                    var size81 = buffer.ReadInt()
                    var result79 = make([][]int, size81)
                    if size81 > 0 {
                        for index80 := 0; index80 < size81; index80++ {
                            var list82 = buffer.ReadIntArray()
                            result79[index80] = list82
                        }
                    }
                    result76[index77] = result79
                }
            }
            result66[result69] = result76
        }
    }
    packet.Mmmm = result66
    var size84 = buffer.ReadInt()
    var result83 = make(map[[]map[int]string][]map[int]string)
    if size84 > 0 {
        for index85 := 0; index85 < size84; index85++ {
            var size88 = buffer.ReadInt()
            var result86 = make([]map[int]string, size88)
            if size88 > 0 {
                for index87 := 0; index87 < size88; index87++ {
                    var map89 = buffer.ReadIntStringMap()
                    result86[index87] = map89
                }
            }
            var size92 = buffer.ReadInt()
            var result90 = make([]map[int]string, size92)
            if size92 > 0 {
                for index91 := 0; index91 < size92; index91++ {
                    var map93 = buffer.ReadIntStringMap()
                    result90[index91] = map93
                }
            }
            result83[result86] = result90
        }
    }
    packet.Mmmmm = result83
    var set94 = buffer.ReadIntArray()
    packet.S = set94
    var size97 = buffer.ReadInt()
    var result95 = make([][][]int, size97)
    if size97 > 0 {
        for index96 := 0; index96 < size97; index96++ {
            var size100 = buffer.ReadInt()
            var result98 = make([][]int, size100)
            if size100 > 0 {
                for index99 := 0; index99 < size100; index99++ {
                    var list101 = buffer.ReadIntArray()
                    result98[index99] = list101
                }
            }
            result95[index96] = result98
        }
    }
    packet.Ss = result95
    var size104 = buffer.ReadInt()
    var result102 = make([][]ObjectA, size104)
    if size104 > 0 {
        for index103 := 0; index103 < size104; index103++ {
            var size107 = buffer.ReadInt()
            var result105 = make([]ObjectA, size107)
            if size107 > 0 {
                for index106 := 0; index106 < size107; index106++ {
                    var result108 = *buffer.ReadPacket(102).(*ObjectA)
                    result105[index106] = result108
                }
            }
            result102[index103] = result105
        }
    }
    packet.Sss = result102
    var set109 = buffer.ReadStringArray()
    packet.Ssss = set109
    var size112 = buffer.ReadInt()
    var result110 = make([]map[int]string, size112)
    if size112 > 0 {
        for index111 := 0; index111 < size112; index111++ {
            var map113 = buffer.ReadIntStringMap()
            result110[index111] = map113
        }
    }
    packet.Sssss = result110
    if buffer.CompatibleRead(beforeReadIndex, length) {
        var result114 = buffer.ReadInt()
        packet.MyCompatible = result114
    }
    if buffer.CompatibleRead(beforeReadIndex, length) {
        var result115 = *buffer.ReadPacket(102).(*ObjectA)
        packet.MyObject = result115
    }
	if length > 0 {
        buffer.SetReadOffset(beforeReadIndex + length)
    }
	return packet
}
