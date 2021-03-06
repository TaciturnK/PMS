package com.pms.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "t_department")
public class Department {
	/**
	 * 唯一标识
	 */
	@Id
	@Column(name = "DEP_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 部门名称
	 */
	@Column(name = "DEP_NAME")
	private String name;

	/**
	 * 部门领导
	 */
	@Column(name = "DEP_LEADER")
	private String leader;

	/**
	 * 领导姓名
	 */
	@Transient
	private String empName;
	/**
	 * 领导电话
	 */
	@Transient
	private String empPhone;

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpPhone() {
		return empPhone;
	}

	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}

	/**
	 * 获取唯一标识
	 * @return DEP_ID - 唯一标识
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置唯一标识
	 * @param id 唯一标识
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * 获取部门名称
	 * @return DEP_NAME - 部门名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置部门名称
	 * @param name 部门名称
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 获取部门领导
	 * @return DEP_LEADER - 部门领导
	 */
	public String getLeader() {
		return leader;
	}

	/**
	 * 设置部门领导
	 * @param leader 部门领导
	 */
	public void setLeader(final String leader) {
		this.leader = leader;
	}

	/** Default constructor */
	public Department() {
	}
}