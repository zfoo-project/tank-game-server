/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.single.boot.godot;

import com.zfoo.protocol.IPacket;
import com.zfoo.storage.model.anno.Id;
import com.zfoo.storage.model.anno.Resource;

/**
 * @author godotg
 * @version 3.0
 */
@Resource
public class GodotResource implements IPacket {

    @Id
    public int id;
    public String path;
    public float randomUpY;
    public float randomDownY;
    public float forwardX;
    public int refreshTime;
    public float refreshAccelerate;
    public String signalBind;
    public String callback;

}
