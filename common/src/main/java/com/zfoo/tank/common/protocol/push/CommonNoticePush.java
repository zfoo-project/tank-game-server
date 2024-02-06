package com.zfoo.tank.common.protocol.push;

import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.tank.common.protocol.room.ExitRoomNotice;
import com.zfoo.tank.common.protocol.room.JoinRoomNotice;

import java.util.ArrayList;
import java.util.List;


public class CommonNoticePush implements Cloneable{

    private List<Long> targets;
    // -----------------------------------------------------------------------------------------------------------------
    private JoinRoomNotice joinRoomNotice;
    private ExitRoomNotice exitRoomNotice;

    public CommonNoticePush() {
    }

    public CommonNoticePush(List<Long> targets) {
        this.targets = targets;
    }


    public List<Object> allNotices() {
        var list = new ArrayList<Object>();
        var fields = ReflectionUtils.notStaticAndTransientFields(this.getClass());
        for(var field : fields) {
            if (field.getName().equals("targets")) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            var notice = ReflectionUtils.getField(field, this);
            if (notice == null) {
                continue;
            }
            list.add(notice);
        }
        return list;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<Long> getTargets() {
        return targets;
    }

    public void setTargets(List<Long> targets) {
        this.targets = targets;
    }

    public JoinRoomNotice getJoinRoomNotice() {
        return joinRoomNotice;
    }

    public void setJoinRoomNotice(JoinRoomNotice joinRoomNotice) {
        this.joinRoomNotice = joinRoomNotice;
    }

    public ExitRoomNotice getExitRoomNotice() {
        return exitRoomNotice;
    }

    public void setExitRoomNotice(ExitRoomNotice exitRoomNotice) {
        this.exitRoomNotice = exitRoomNotice;
    }
}
