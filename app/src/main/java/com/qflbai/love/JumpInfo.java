package com.qflbai.love;

import java.util.List;

/**
 * @author: qflbai
 * @CreateDate: 2019/11/1 16:04
 * @Version: 1.0
 * @description:
 */
public class JumpInfo {
    private int position;
    private List<PhotoInfo> photoInfos;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<PhotoInfo> getPhotoInfos() {
        return photoInfos;
    }

    public void setPhotoInfos(List<PhotoInfo> photoInfos) {
        this.photoInfos = photoInfos;
    }
}
