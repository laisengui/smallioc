/*
 * Copyright (c) 2005, 2017, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.h.a;

import com.h.g.C;

import cn.lsg.smallioc.annotation.Inject;
import cn.lsg.smallioc.annotation.Member;

/**
 * 描述
 * @author Norton Lai
 * @created 2017-12-24 上午12:45:56
 */
@Member(single=false)
public class B implements O {

    @Inject(id="jijij")
    public A a;
    
    @Inject
    public C c;
    
    public String f;

    public B() {
        super();
        System.out.println("B实例被创建");
    }

    /**
     * 描述
     * @author Norton Lai
     * @created 2017-12-24 下午1:16:22
     * @see com.h.a.O#speck()
     */
    @Override
    public void speck() {
        System.out.println("B说话");
    }
    
    
}
