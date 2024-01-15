/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.protocol.javassist;

import javassist.*;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.*;

public class JavassistTest {
    public static final int INTERVAL_TIME = 1000;

    public static final int A_CONSTANT = 99999;
    public static final int B_CONSTANT = 888888888;
    public static final int C_CONSTANT = 7777777;

    @Test
    public void testD() throws NotFoundException, CannotCompileException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassPool classPool = ClassPool.getDefault();

        CtClass ctClass = classPool.makeClass("com.zfoo.protocol.javassist.DGetClass");

        ctClass.addInterface(classPool.get(IDGet.class.getCanonicalName()));

        CtField a = new CtField(classPool.get(int.class.getCanonicalName()), "a", ctClass);
        a.setModifiers(Modifier.PRIVATE);
        ctClass.addField(a);

        CtField b = new CtField(classPool.get(int.class.getCanonicalName()), "b", ctClass);
        b.setModifiers(Modifier.PRIVATE);
        ctClass.addField(b);

        CtField c = new CtField(classPool.get(int.class.getCanonicalName()), "c", ctClass);
        c.setModifiers(Modifier.PRIVATE);
        ctClass.addField(c);

        CtConstructor ctConstructor = new CtConstructor(null, ctClass);
        ctConstructor.setBody("{}");
        ctClass.addConstructor(ctConstructor);

        CtMethod aGetMethod = new CtMethod(classPool.get(int.class.getCanonicalName()), "getA", null, ctClass);
        aGetMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder aGetBuilder = new StringBuilder();
        aGetBuilder.append("{return this.a;}");
        aGetMethod.setBody(aGetBuilder.toString());
        ctClass.addMethod(aGetMethod);

        CtMethod aSetMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "setA", classPool.get(new String[]{int.class.getCanonicalName()}), ctClass);
        aGetMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder aSetBuilder = new StringBuilder();
        aSetBuilder.append("{this.a=$1;}");
        aSetMethod.setBody(aSetBuilder.toString());
        ctClass.addMethod(aSetMethod);

        CtMethod bGetMethod = new CtMethod(classPool.get(int.class.getCanonicalName()), "getB", null, ctClass);
        bGetMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder bGetBuilder = new StringBuilder();
        bGetBuilder.append("{return this.b;}");
        bGetMethod.setBody(bGetBuilder.toString());
        ctClass.addMethod(bGetMethod);

        CtMethod bSetMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "setB", classPool.get(new String[]{int.class.getCanonicalName()}), ctClass);
        aGetMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder bSetBuilder = new StringBuilder();
        bSetBuilder.append("{this.b=$1;}");
        bSetMethod.setBody(bSetBuilder.toString());
        ctClass.addMethod(bSetMethod);

        CtMethod cGetMethod = new CtMethod(classPool.get(int.class.getCanonicalName()), "getC", null, ctClass);
        cGetMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder cGetBuilder = new StringBuilder();
        cGetBuilder.append("{return this.c;}");
        cGetMethod.setBody(cGetBuilder.toString());
        ctClass.addMethod(cGetMethod);

        CtMethod cSetMethod = new CtMethod(classPool.get(void.class.getCanonicalName()), "setC", classPool.get(new String[]{int.class.getCanonicalName()}), ctClass);
        aGetMethod.setModifiers(Modifier.PUBLIC);
        StringBuilder cSetBuilder = new StringBuilder();
        cSetBuilder.append("{this.c=$1;}");
        cSetMethod.setBody(cSetBuilder.toString());
        ctClass.addMethod(cSetMethod);

        Class<?> clazz = ctClass.toClass(IDGet.class);
        Constructor<?> constructor = clazz.getConstructor();
        IDGet d = (IDGet) constructor.newInstance();

        d.setA(A_CONSTANT);
        d.setB(B_CONSTANT);
        d.setC(C_CONSTANT);
    }



}

