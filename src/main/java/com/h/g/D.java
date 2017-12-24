/*
 * Copyright (c) 2005, 2017, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.h.g;

import com.h.a.B;

import cn.lsg.smallioc.annotation.Member;

/**
 * 描述
 * @author Norton Lai
 * @created 2017-12-24 上午12:46:16
 */
@Member
public class D {

    
    public B b;
    
    public C c;

    public D() {
        super();
        System.out.println("D实例被创建");
    }
    
    
}
