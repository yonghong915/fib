package com.fib.cmms.ctrler;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.cmms.entity.ChineseMedicine;
import com.fib.cmms.service.ChineseMedicineService;

@RestController
@RequestMapping(value = "/chineseMedicine")
public class ChineseMedicineCtrler {
	private final ChineseMedicineService chineseMedicineService;

	public ChineseMedicineCtrler(ChineseMedicineService chineseMedicineService) {
		this.chineseMedicineService = chineseMedicineService;
	}

	@GetMapping(value = "/list")
	public List<ChineseMedicine> findList() {
		System.out.println("查询所有数据:");
		return chineseMedicineService.list();
	}
}
