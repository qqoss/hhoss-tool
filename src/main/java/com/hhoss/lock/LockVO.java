package com.hhoss.lock;

import java.util.Date;

import com.hhoss.data.Item;

public class LockVO extends Item<LockVO> {
	private static final long serialVersionUID = -3416878797783121918L;
	private long rid;
	private long uid;
	private Date active;
	private Date expire;

	public LockVO() {}
	public LockVO(int mold, String code) {
		setMold(mold);
		setCode(code);
	}

	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Date getActive() {
		return active;
	}

	public void setActive(Date active) {
		this.active = active;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

}
