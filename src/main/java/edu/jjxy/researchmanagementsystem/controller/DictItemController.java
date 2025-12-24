package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.DictItem;
import edu.jjxy.researchmanagementsystem.service.DictItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dict/item")
public class DictItemController {

    private final DictItemService service;

    public DictItemController(DictItemService service) {
        this.service = service;
    }

    @GetMapping("/page")
    public Result<PageResult<DictItem>> page(@RequestParam int page,
                                             @RequestParam int size,
                                             @RequestParam(required = false) String typeCode,
                                             @RequestParam(required = false) String itemCode,
                                             @RequestParam(required = false) String itemName,
                                             @RequestParam(required = false) Integer status) {
        return Result.ok(service.page(page, size, typeCode, itemCode, itemName, status));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody DictItem t) {
        service.add(t);
        return Result.ok(null);
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody DictItem t) {
        service.update(t);
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok(null);
    }
}
