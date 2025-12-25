package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Award;
import edu.jjxy.researchmanagementsystem.entity.User;
import edu.jjxy.researchmanagementsystem.service.AwardService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
                                          @RequestParam(required=false) Long projectId,
                                          HttpServletRequest request){
        User current = (User) request.getAttribute("currentUser");
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            List<Award> all = service.listByPersonId(current.getId());
            List<Award> filtered = new ArrayList<>();
            for (Award a : all) {
                if (awardName != null && !awardName.isEmpty() && (a.getAwardName() == null || !a.getAwardName().contains(awardName))) {
                    continue;
                }
                if (projectId != null && (a.getProjectId() == null || !a.getProjectId().equals(projectId))) {
                    continue;
                }
                filtered.add(a);
            }
            int fromIndex = (page - 1) * size;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
            int toIndex = Math.min(fromIndex + size, filtered.size());
            List<Award> pageList = fromIndex >= filtered.size() ? new ArrayList<>() : filtered.subList(fromIndex, toIndex);
            return Result.ok(new PageResult<>((long) filtered.size(), pageList));
        }
        return Result.ok(service.page(page, size, awardName, projectId));
    }

    @PostMapping public Result<Long> add(@RequestBody Award a){ return Result.ok(service.add(a)); }
    @PutMapping public Result<Void> update(@RequestBody Award a){ service.update(a); return Result.ok(null); }
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Long id){ service.delete(id); return Result.ok(null); }
    @GetMapping("/{id}") public Result<Award> get(@PathVariable Long id){ return Result.ok(service.get(id)); }
    @GetMapping("/listByPersonId")
    public Result<List<Award>> listByPersonId(@RequestParam Long personId, HttpServletRequest request) {
        User current = (User) request.getAttribute("currentUser");
        Long effectivePersonId = personId;
        if (current != null && "USER".equalsIgnoreCase(current.getRole())) {
            effectivePersonId = current.getId();
        }
        return Result.ok(service.listByPersonId(effectivePersonId));
    }

    @PostMapping("/bind")
    public Result<Void> bind(@RequestParam Long awardId,
                             @RequestParam Long projectId,
                             HttpServletRequest request) {
        service.bindToProject(awardId, projectId);
        return Result.ok(null);
    }

    @PostMapping("/unbind")
    public Result<Void> unbind(@RequestParam Long awardId,
                               HttpServletRequest request) {
        service.unbindFromProject(awardId);
        return Result.ok(null);
    }
}
