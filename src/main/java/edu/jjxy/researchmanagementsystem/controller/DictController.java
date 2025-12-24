package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.DictItem;
import edu.jjxy.researchmanagementsystem.service.DictItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    private final DictItemService dictItemService;

    public DictController(DictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    // 业务页面使用：dictItems(typeCode)
    @GetMapping("/items")
    public Result<List<DictItem>> items(@RequestParam String typeCode) {
        return Result.ok(dictItemService.items(typeCode));
    }
}
