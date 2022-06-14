package com.web.test.application.service;

import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.RuleSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RulesService {
    @Autowired
    BaseMapper baseMapper;

    static ConcurrentMap<String, RuleSingleton> RULES_MAP = new ConcurrentHashMap<>();

    public List<String> queryFullNameList() {
        Set<String> hashSet = RULES_MAP.keySet();
        List<String> res = new ArrayList<>();
        // res.addAll(hashSet);
        RULES_MAP.forEach((a,b)->{
            res.add(b.getFullName());
        });
        return res;
    }

    public Set<String> queryCNNameSet() {
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            hashSet.add(r.getCnName());
        });
        return hashSet;
    }


    public Set<String> queryFullCodesSet() {
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            hashSet.add(r.getFullCode());
        });
        return hashSet;
    }



    public static ConcurrentMap getRulesMap() {
        return RULES_MAP;
    }
}
