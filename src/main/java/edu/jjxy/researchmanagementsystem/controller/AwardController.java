package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Award;
import edu.jjxy.researchmanagementsystem.service.AwardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/award")
public class AwardController {
    private final AwardService service;
    public AwardController(AwardService service){ this.service = service; }

    @GetMapping("/page")
    public Result<PageResult<Award>> page(@RequestParam(defaultValue="1") int page,
                                          @RequestParam(defaultValue="10") int size,
                                          @RequestParam(required=false) String awardName,
                                          @RequestParam(required=false) Long projectId){
        return Result.ok(service.page(page, size, awardName, projectId));
    }

    @PostMapping public Result<Long> add(@RequestBody Award a){ return Result.ok(service.add(a)); }
    @PutMapping public Result<Void> update(@RequestBody Award a){ service.update(a); return Result.ok(null); }
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Long id){ service.delete(id); return Result.ok(null); }
    @GetMapping("/{id}") public Result<Award> get(@PathVariable Long id){ return Result.ok(service.get(id)); }
    @GetMapping("/listByPersonId")
    public Result<List<Award>> listByPersonId(@RequestParam Long personId) {
        return Result.ok(service.listByPersonId(personId));
    }
}
