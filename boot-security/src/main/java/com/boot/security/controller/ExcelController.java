package com.boot.security.controller;

import com.boot.security.util.ExcelUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excels")
public class ExcelController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/sql-count")
    public Integer checkSql(String sql) {
        //sql格式
        sql = getAndCheckSql(sql);
        //查询该sql返回结果的数量
        Integer count=0;
        try {
             count = jdbcTemplate.queryForObject("select count(*) from (" + sql + ")t", Integer.class);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }

        return count;
    }


    private String getAndCheckSql(String sql) {
        sql=sql.trim().toLowerCase();
        if(sql.endsWith(";")){
            sql.substring(0,sql.indexOf(";"));
        }
        if(!sql.startsWith("select")){
            throw new IllegalArgumentException("仅仅支持查询语句");
        }
        return sql;
    }

    @PostMapping("/show-datas")
    @PreAuthorize("hasAuthority('excel:show:datas')")
    public List<Object[]> showData(String sql) {
        sql = getAndCheckSql(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(CollectionUtils.isNotEmpty(list)){
            //获得table的字段名字
            Map<String, Object> map = list.get(0);
            String[]headers=new String[map.size()];
            int i=0;
            for(String s:map.keySet()){
                headers[i++]=s;
            }
            List<Object[]>lists=new ArrayList<>();
            //方数据
            for(Map<String,Object> m:list){
                Object[] objects = new Object[headers.length];
                for(int j=0;j<m.size();j++){
                    objects[j]=m.get(headers[j]);
                }
                lists.add(objects);
            }
            return lists;
        }
        else{
            return Collections.emptyList();
        }
        }


    @PostMapping
    @PreAuthorize("hasAuthority('excel:down')")
    public void downloadExcel(String sql, String fileName, HttpServletResponse response) {
        sql = getAndCheckSql(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            Map<String, Object> map = list.get(0);

            String[] headers = new String[map.size()];
            int i = 0;
            for (String key : map.keySet()) {
                headers[i++] = key;
            }

            List<Object[]> datas = new ArrayList<>(list.size());
            for (Map<String, Object> m : list) {
                Object[] objects = new Object[headers.length];
                for (int j = 0; j < headers.length; j++) {
                    objects[j] = m.get(headers[j]);
                }

                datas.add(objects);
            }

            ExcelUtil.excelExport(
                    fileName == null || fileName.trim().length() <= 0 ? DigestUtils.md5Hex(sql) : fileName, headers,
                    datas, response);
        }
    }

}
