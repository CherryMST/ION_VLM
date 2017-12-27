package com.nokia.vlm.entity;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/25 23:26
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/25
 * @更新描述 ${TODO}
 */

public class Bean {
    private boolean isChecked;
    private int drawable = 0;


    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name = "";

    public Bean(boolean isChecked, int drawable) {
        this.isChecked = isChecked;
        this.drawable = drawable;
    }
    public Bean(boolean isChecked, int drawable, String name) {
        this.isChecked = isChecked;
        this.drawable = drawable;
        this.name = name;
    }

    public Bean(){

    }
    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }



    public Bean(boolean isCheched) {
        this.isChecked = isCheched;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
