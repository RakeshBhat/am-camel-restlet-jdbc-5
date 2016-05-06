package student;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DbSelectController {
	private static final Logger logger = LoggerFactory.getLogger(DbSelectController.class);
	@Autowired NamedParameterJdbcTemplate mySqlParamJdbcTemplate;
	
	@Value("${sql.spring.hol1.departmentWhereId}") private String departmentWhereId;
	@Value("${sql.spring.hol1.departmentIcd10Group}") private String departmentIcd10Group;
	@RequestMapping(value = "/r/readDepartmentIcd10Group-{m1}-{m2}-{departmentId}", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readDepartmentIcd10Group(
			@PathVariable Integer m1
			,@PathVariable Integer m2
			,@PathVariable Integer departmentId
			,Principal userPrincipal) {
		System.out.println("--------readDepartmentIcd10Group---------------");
		Map<String, Object> map = new HashMap<>();
		map.put("m1", m1);
		map.put("m2", m2);
		map.put("departmentId", departmentId);

		Map<String, Integer> mmp = new HashMap<>();
		mmp.put("min_month", m1);
		mmp.put("max_month", m2);
		mmp.put("department_id", departmentId);
		Util.addCommonParameter(mmp);
		logger.debug(" \n "+mmp.toString()+" \n SQL length "+departmentIcd10Group.length());
		System.out.println(" \n "+mmp.toString()+" \n SQL length "+departmentIcd10Group.length());
		List<Map<String, Object>> bedDayOfMonthMySql 
		= mySqlParamJdbcTemplate.queryForList(departmentIcd10Group, mmp);

		map.put("bedDayOfMonthMySql", bedDayOfMonthMySql);

		Map<String, Integer> mmp2 = new HashMap<>();
		mmp2.put("department_id", departmentId);
		logger.debug(mmp+" "+departmentWhereId);
		System.out.println(mmp+" "+departmentWhereId);
		Map<String, Object> department = mySqlParamJdbcTemplate.queryForMap(departmentWhereId,mmp2);

		map.put("department", department);
		return map;
	}
	
	@Value("${sql.spring.hol1.bedDay}") private String sqlSpringHol1BedDay;

	@RequestMapping(value = "/r/readBedDayDepartmentMySql-{m1}-{m2}", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readBedDayDepartmentMySql(
			@PathVariable Integer m1
			,@PathVariable Integer m2
			,Principal userPrincipal) {
		logger.debug("----------------------- ");
		System.out.println("----------readBedDayDepartmentMySql------------- ");
		Map<String, Object> map = new HashMap<>();
		map.put("m1", m1);
		map.put("m2", m2);
		List<Map<String, Object>> bedDayOfMonthMySql = readBedDayMySql(m1,m2,userPrincipal);
		map.put("bedDayOfMonthMySql", bedDayOfMonthMySql);
		if(m1 < m2) {
			List<Object> arrayList = new ArrayList<>();
			for (int i = m1; i <= m2; i++) {
				logger.debug("----------------------- "+i);
				System.out.println("----------------------- "+i);
				List<Map<String, Object>> readBedDayOfMonthMySql = readBedDayMySql(i,i,userPrincipal);
				arrayList.add(readBedDayOfMonthMySql);
			}
			map.put("bedDayDepartmentMySql", arrayList);
		}
		return map;
	}

	@RequestMapping(value = "/r/readBedDayMySql-{m1}-{m2}", method = RequestMethod.GET)
	public  @ResponseBody List<Map<String, Object>> readBedDayMySql(
			 @PathVariable Integer m1
			 ,@PathVariable Integer m2
			,Principal userPrincipal) {
		Map<String, Integer> mmp = new HashMap<>();
		mmp.put("min_month", m1);
		mmp.put("max_month", m2);
		Util.addCommonParameter(mmp);
		List<Map<String, Object>> bedDayOfMonthMySql 
		= mySqlParamJdbcTemplate.queryForList(sqlSpringHol1BedDay, mmp);
		return bedDayOfMonthMySql;
	}

}
