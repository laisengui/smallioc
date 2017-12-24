/*
 * Copyright (c) 2005, 2017, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.h.a;

import cn.lsg.smallioc.annotation.Inject;
import cn.lsg.smallioc.annotation.Member;

/**
 * 描述
 * @author Norton Lai
 * @created 2017-12-24 上午12:45:48
 */
@Member(id="jijij")
public class A implements O{
    
    @Inject
    public B b;

    public A() {
        super();
        System.out.println("A实例被创建");
    }

    /**
     * 描述
     * @author Norton Lai
     * @created 2017-12-24 下午1:16:05
     * @see com.h.a.O#speck()
     */
    @Override
    public void speck() {
        System.out.println("a说话");
    }
    
    
    

}
