/*
 * Copyright (c) 2005, 2017, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.h.g;

import com.h.a.A;

import cn.lsg.smallioc.annotation.Inject;
import cn.lsg.smallioc.annotation.Member;

/**
 * 描述
 * @author Norton Lai
 * @created 2017-12-24 上午12:46:09
 */
@Member(single=false)
public class C {

    @Inject
    public A a;

    public C() {
        super();
        System.out.println("C实例被创建");
    }
    
    
}
