package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.DictType;
import edu.jjxy.researchmanagementsystem.service.DictTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict/type")
public class DictTypeController {

    private final DictTypeService service;

    public DictTypeController(DictTypeService service) {
        this.service = service;
    }

    @GetMapping("/page")
    public Result<PageResult<DictType>> page(@RequestParam int page,
                                             @RequestParam int size,
                                             @RequestParam(required = false) String typeCode,
                                             @RequestParam(required = false) String typeName,
                                             @RequestParam(required = false) Integer status) {
        return Result.ok(service.page(page, size, typeCode, typeName, status));
    }

    @GetMapping("/list")
    public Result<List<DictType>> list() {
        return Result.ok(service.listAll());
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody DictType t) {
        service.add(t);
        return Result.ok(null);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody DictType t) {
        service.update(t);
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok(null);
    }
}
