package com.suncompass.basic.codegen.controller;
import com.suncompass.basic.codegen.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.Serializable;

public class BaseController<T> {

    @Autowired
    private BaseService<T> baseService;

    @PostMapping("/list")
    public ResponseEntity selectAll(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestBody(required = false) T t) {
        return ResponseEntity.ok(baseService.findAll(pageNo, pageSize, t));
    }

    @RequestMapping("/get")
    public ResponseEntity get(@RequestParam("id") Serializable id) throws IOException {
        return ResponseEntity.ok(baseService.get(id));
    }

    @RequestMapping("/insert")
    public ResponseEntity insert(@RequestBody T t) throws IOException {
        return ResponseEntity.ok(baseService.insert(t));
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity delete(@RequestParam("id") Serializable id) {
        return ResponseEntity.ok(baseService.removeId(id));
    }

    @RequestMapping("/update")
    public ResponseEntity update(@RequestBody T t) throws IOException {
        return ResponseEntity.ok(baseService.update(t));
    }

}
