package com.springjpa.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springjpa.bean.CustomerBean;
import com.springjpa.model.Customer;
import com.springjpa.service.CustomerService;
import com.springjpa.service.ReportService;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class CustomerController {
	@Autowired CustomerService customerService;
	@Autowired ReportService reportService;
	
	public static final String path = "customer";
	private static final String PARAM_ID = "id";
	
	/*@RequestMapping("/")
	public String Show(Model model ,HttpServletRequest httpServletRequest){
		System.out.println("Test Index");
		return "login";
	}*/
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String Show(Model model ,HttpServletRequest httpServletRequest){
		List<Customer> listCustomer =  customerService.listCustomer();
		model.addAttribute("listCustomer",listCustomer);
		return path + "/" +"view-customer";
	}
	
	@RequestMapping(value="/addCustomer", method=RequestMethod.GET)
	public String ShowAddCustomer(HttpServletRequest httpServletRequest,Model model){
		return path + "/" + "new-customer";
	}
	
	@RequestMapping(value="/editCustomer/{id}", method=RequestMethod.GET)
	public String ShowEditCustomer(HttpServletRequest httpServletRequest,Model model,final @PathVariable(PARAM_ID) long id){
		Customer customer = (Customer) customerService.findByCustomerId(id);
		model.addAttribute("customer",customer);
		return path + "/" + "edit-customer";
	}
	
	@RequestMapping(value="/addCustomer/new", method=RequestMethod.POST)
	public String AddNew(HttpServletRequest httpServletRequest,Model model,final @Valid CustomerBean params,RedirectAttributes redirectAttrs)throws Exception{
		if (customerService.isRecordFull()){
			redirectAttrs.addFlashAttribute("message" , "record customer full");
			return "redirect:" + "/" +"addCustomer";
		}
		redirectAttrs.addFlashAttribute("message" , "Success add customer");
		customerService.saveCustomer(params); 
		return "redirect:" + "/" +"addCustomer";
	}
	
	@RequestMapping(value="/deleteCustomer/{id}", method=RequestMethod.POST)
	public String DeleteCustomer(HttpServletRequest httpServletRequest,Model model,final @PathVariable(PARAM_ID) long id,RedirectAttributes redirectAttrs){
		customerService.deleteById(id); 
		redirectAttrs.addFlashAttribute("message" , "delete record success");
		return "redirect:" + "/"; 
	}
	
	@RequestMapping(value="/editCustomer/{id}", method=RequestMethod.POST)
	public String EditCustomer(HttpServletRequest httpServletRequest,Model model,final @PathVariable(PARAM_ID) long id,@Valid CustomerBean params,RedirectAttributes redirectAttrs){
		customerService.updateCustomer(id,params); 
		redirectAttrs.addFlashAttribute("message" , "Edit record customer success");
		return "redirect:" + "/"+"editCustomer/"+id;
	}
	
	@RequestMapping(value = "/report", method=RequestMethod.GET)
	@ResponseBody
	public String ShowReport(Model model ,HttpServletRequest httpServletRequest,HttpServletResponse response) throws JRException, IOException{
		List<Map<String, ?>> listCustomer =  reportService.findAllReportCustomer();
		model.addAttribute("listCustomer",listCustomer);
		ServletContext servletContext = httpServletRequest.getSession().getServletContext();
	    String sourceFileName = servletContext.getRealPath("/WEB-INF/template/ReportListCustomer.jrxml");
		InputStream inputStream = new FileInputStream(new File(sourceFileName));
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listCustomer);
		DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
	    JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
		JasperReport report = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(report,null,jrDataSource);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		return path + "/" +"listCustomer";
	}
	
}
