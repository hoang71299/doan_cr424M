package com.example.huynhtrannhathoang_bai16_karaoke;

public class Item {
    private String maso,tieude;
    private Integer thich;
    public Item(String maso, String tieude, Integer thich) {
        this.maso = maso;
        this.tieude = tieude;
        this.thich = thich;
    }
    /**
     * @return the tieude
     */
    public String getTieude() {
        return tieude;
    }
    /**
     * @param tieude the tieude to set
     */
    public void setTieude(String tieude) {
        this.tieude = tieude;
    }
    /**
     * @return the maso
     */
    public String getMaso() {
        return maso;
    }
    /**
     * @param maso the maso to set
     */
    public void setMaso(String maso) {
        this.maso = maso;
    }
    /**
     * @return the thich
     */
    public Integer getThich() {
        return thich;
    }
    /**
     * @param thich the thich to set
     */
    public void setThich(Integer thich) {
        this.thich = thich;
    }

}
