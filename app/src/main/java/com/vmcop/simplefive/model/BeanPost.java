package com.vmcop.simplefive.model;
import com.google.gson.annotations.SerializedName;

public class BeanPost {
    @SerializedName("title")
    private String title;
    @SerializedName("image_name")
    private String image_name;
    @SerializedName("nguon_tk")
    private String nguon_tk;
	@SerializedName("is_default_show")
	private Boolean is_default_show;
    @SerializedName("ngay_tao")
    private String ngay_tao;

	public BeanPost(String title, String nguyen_lieu, String cach_lam, String image_name, String nguon_tk, Boolean is_default_show, String ngay_tao) {
        this.title = title;
        this.image_name = image_name;
        this.nguon_tk = nguon_tk;
		this.is_default_show = is_default_show;
        this.ngay_tao = ngay_tao;
    }
	
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage_name() {
		return image_name;
	}
	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}
	public String getNguon_tk() {
		return nguon_tk;
	}
	public void setNguon_tk(String nguon_tk) {
		this.nguon_tk = nguon_tk;
	}
	public Boolean getIs_default_show() { return is_default_show;}
	public void setIs_default_show(Boolean is_default_show) { this.is_default_show = is_default_show;}
	public String getNgay_tao() {
		return ngay_tao;
	}
	public void setNgay_tao(String ngay_tao) { this.ngay_tao = ngay_tao;}
}
