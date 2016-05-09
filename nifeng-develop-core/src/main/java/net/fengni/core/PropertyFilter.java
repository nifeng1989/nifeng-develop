package net.fengni.core;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 与具体ORM实现无关的属性过滤条件封装类.
 * 
 * PropertyFilter主要记录页面中简单的搜索过滤条件,比Hibernate的Criterion要简单很多.
 * 可按项目扩展其他对比方式如大于、小于及其他数据类型如数字和日期.
 * 
 * @author calvin
 */
public class PropertyFilter {

	/**
	 * 多个属性间OR关系的分隔符.
	 */
	public static final String OR_SEPARATOR = "__";

	/**
	 * 属性比较类型. 
	 * 当类型为IN类型时，Object value必须为Collection类型
	 */
	public enum MatchType {
		EQ, LIKE, IN, NE, GE, LE, GT, LT, IS, NOT_IN;
	}

	public Map getMatchTypeMap() {
		Map map = new HashMap();
		map.put(MatchType.EQ, "等于");
		map.put(MatchType.LIKE, "包含");
		map.put(MatchType.IN, "包含");
		map.put(MatchType.NOT_IN, "不包含");
		map.put(MatchType.NE, "不等于");

		map.put(MatchType.GE, "大于等于");
		map.put(MatchType.LE, "小于等于");
		map.put(MatchType.GT, "大于");
		map.put(MatchType.LT, "小于");

		map.put(MatchType.IS, "");

		return map;
	}

	private String propertyName;
	private Object value;
	private MatchType matchType = MatchType.EQ;

	public PropertyFilter() {
	}

	public PropertyFilter(final String propertyName, final Object value, final MatchType matchType) {
		this.propertyName = propertyName;
		this.value = value;
		this.matchType = matchType;
	}

	/**
	 * 获取属性名称,可用'__'分隔多个属性,此时属性间是or的关系.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * 设置属性名称,可用'__'分隔多个属性,此时属性间是or的关系.
	 */
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public void setMatchType(final MatchType matchType) {
		this.matchType = matchType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PropertyFilter other = (PropertyFilter) obj;
		if (matchType == null) {
			if (other.matchType != null)
				return false;
		} else if (!matchType.equals(other.matchType))
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	protected void buildCondition(StringBuilder condition,Map<String,Object> map) {
		Assert.hasText(propertyName, "propertyName不能为空");
		map.put(propertyName,value);
		if (MatchType.EQ.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" =:").append(propertyName);
		} else if (MatchType.NE.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" !=:").append(propertyName);
		} else if (MatchType.GT.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" >:").append(propertyName);
		} else if (MatchType.GE.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" >=").append(propertyName);
		} else if (MatchType.LT.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" <").append(propertyName);
		} else if (MatchType.LE.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" <=").append(propertyName);
		} else if (MatchType.LIKE.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" like ").append(propertyName);
		} else if (MatchType.IN.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" in (:").append(propertyName).append(")");
		} else if (MatchType.NOT_IN.equals(matchType)) {
			condition.append(" and ").append(propertyName).append(" not in (:").append(propertyName).append(")");
		} else if (MatchType.IS.equals(matchType)) {
			map.remove(propertyName);
			if (value == null || "null".equalsIgnoreCase(value.toString()))
				condition.append(" and").append(propertyName).append(" is null");
			else
				condition.append(" and").append(propertyName).append(" is not null");
		}
	}
}
