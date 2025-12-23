package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.DictItem;
import edu.jjxy.researchmanagementsystem.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping("/items")
    public Result<List<DictItem>> items(@RequestParam String typeCode) {
        return Result.ok(dictService.listItemsByType(typeCode));
    }
}
