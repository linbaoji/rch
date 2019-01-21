package com.rch.entity;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ComparatorImpl implements Comparator<CityInfoEntity> {

    @Override
    public int compare(CityInfoEntity entity, CityInfoEntity t1) {
        String s1 = entity.getCityName();
        String s2 = t1.getCityName();
        return Collator.getInstance(Locale.CHINESE).compare(s1, s2); //从a-z的排
    }
}
