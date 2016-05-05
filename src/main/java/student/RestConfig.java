/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package student;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;

public class RestConfig extends RouteBuilder {

	@Value("${sql.movedepartmentpatient.day}")
	private String sqlMovedepartmentpatientDay;

	@Value("${sql.movedepartmentpatient.fields}")
	private String sqlMovedepartmentpatientFields;

	@Override
	public void configure() {
		System.out.println("----configure---------------------");
		List<String> items = Arrays.asList(sqlMovedepartmentpatientFields.split("\\s*,\\s*"));
		System.out.println(items);
		System.out.println("----configure---------------------");
	
		rest("/departments")
		.get().to("direct:getDepartments");
		from("direct:getDepartments")
		.setBody(simple("select * from hol2.department"))
		.to("jdbc:dataSourceHol2Eih").marshal().json()
		;

		rest("/movePatient")
			.get("/read-{year}-{month}-{day}").to("direct:readDayMovePatient")
			.put("/updateMoveDepartmentPatien-{id}-{field}-{value}").to("direct:updateMoveDepartmentPatien")
			.put("/updateMoveDepartmentPatien-{id}-{field}-").to("direct:updateMoveDepartmentPatienNull");

		from("direct:updateMoveDepartmentPatienNull")
		.log("update hol2.movedepartmentpatient set ${header.field}=null where movedepartmentpatient_id = ${header.id}")
		.setBody(simple("update hol2.movedepartmentpatient set ${header.field}=null where movedepartmentpatient_id = ${header.id}"))
		.to("jdbc:dataSourceHol2Eih")
		;
		from("direct:updateMoveDepartmentPatien")
		.log("update hol2.movedepartmentpatient set ${header.field}='${header.value}' where movedepartmentpatient_id = ${header.id}")
		.setBody(simple("update hol2.movedepartmentpatient set ${header.field}='${header.value}' where movedepartmentpatient_id = ${header.id}"))
		.to("jdbc:dataSourceHol2Eih")
		;

		from("direct:readDayMovePatient")
		.setBody(simple(Util.replace(sqlMovedepartmentpatientDay)))
		.log("\n" + Util.replace(sqlMovedepartmentpatientDay) )
		.to("jdbc:dataSourceHol2Eih")
		.marshal().json(JsonLibrary.Jackson)
		;

		rest("/persons")
		.post().to("direct:postPersons")
		.get().to("direct:getPersons")
		.get("/{personId}").to("direct:getPersonId")
		.put("/{personId}").to("direct:putPersonId")
		.delete("/{personId}").to("direct:deletePersonId");

		from("direct:postPersons")
		.setBody(simple("insert into person(firstName, lastName) values('${header.firstName}','${header.lastName}')"))
		.to("jdbc:dataSource")
		.setBody(simple("select * from person where id in (select max(id) from person)"))
		.to("jdbc:dataSource");

		from("direct:getPersons")
		.setBody(simple("select * from person"))
		.to("jdbc:dataSource");

		from("direct:getPersonId")
		.setBody(simple("select * from person where id = ${header.personId}"))
		.to("jdbc:dataSource");

		from("direct:putPersonId")
		.setBody(simple("update person set firstName='${header.firstName}', lastName='${header.lastName}' where id = ${header.personId}"))
		.to("jdbc:dataSource");

		from("direct:deletePersonId")
		.setBody(simple("delete from person where id = ${header.personId}"))
		.to("jdbc:dataSource");

	}

}
