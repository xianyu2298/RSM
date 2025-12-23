package edu.jjxy.researchmanagementsystem.controller;

import edu.jjxy.researchmanagementsystem.common.PageResult;
import edu.jjxy.researchmanagementsystem.common.Result;
import edu.jjxy.researchmanagementsystem.entity.Person;
import edu.jjxy.researchmanagementsystem.service.PersonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/page")
    public Result<PageResult<Person>> page(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String name) {
        return Result.ok(personService.page(page, size, name));
    }

    @PostMapping
    public Result<Long> add(@RequestBody Person p) {
        return Result.ok(personService.add(p));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Person p) {
        personService.update(p);
        return Result.ok(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return Result.ok(null);
    }

    @GetMapping("/{id}")
    public Result<Person> get(@PathVariable Long id) {
        return Result.ok(personService.get(id));
    }
}
