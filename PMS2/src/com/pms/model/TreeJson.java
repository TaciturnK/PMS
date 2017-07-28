package com.pms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * easui�е�tree_data.json����,ֻ����һ��root�ڵ�
 * [{   
 *   "id":1,   
 *  "text":"Folder1",   
 *   "iconCls":"icon-save",   
 *   "children":[{   
 *       "text":"File1",   
 *       "checked":true  
 *   }]   
 * }] 
 * �ṩ��̬����formatTree(List<TreeJson> list) ���ؽ��
 * TreeJson.formatTree(treeJsonlist) ;
 * @author Taowd
 *
 */
public class TreeJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String pid;
	private String text;
	// ͼ��·��
	private String iconCls;
	private String state;
	private boolean checked;
	private List<TreeJson> children = new ArrayList<TreeJson>();

	/******** setter and getter **********/
	/**
	 * 
	 * <p>��  �� ��: TreeJson.java</p>
	 * <p>��        ��: ��List����ת��Tree���ṹ</p>
	 * <p>��  ��  ֵ: List<TreeJson></p>
	 * <p>��        �ߣ� Taowd</p>
	 * <p>����ʱ�� �� 2017-4-3 ����7:45:29</p>
	 */
	public static List<TreeJson> formatTree(List<TreeJson> list) {

		TreeJson root = new TreeJson();
		TreeJson node = new TreeJson();
		List<TreeJson> treelist = new ArrayList<TreeJson>();// ƴ�պõ�json��ʽ������
		List<TreeJson> parentnodes = new ArrayList<TreeJson>();// parentnodes������еĸ��ڵ�

		if (list != null && list.size() > 0) {
			for (int i = 1; i < list.size(); i++) {
				// Ѱ�Ҹ��ڵ�--ֻ����һ�����ڵ�
				if (list.get(i).pid.equals("0")) {
					root = list.get(i);
				}
			}
			// ѭ����������ѯ�����нڵ�
			for (int i = 1; i < list.size(); i++) {

				node = list.get(i);
				if (node.getPid().equals(root.getId())) {
					// Ϊtree root �����ӽڵ�
					parentnodes.add(node);
					root.getChildren().add(node);
				} else {
					// ��ȡroot�ӽڵ�ĺ��ӽڵ�
					getChildrenNodes(parentnodes, node);
					parentnodes.add(node);
				}
			}
		}
		treelist.add(root);
		return treelist;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<TreeJson> getChildren() {
		return children;
	}

	public void setChildren(List<TreeJson> children) {
		this.children = children;
	}

	public TreeJson() {
		super();
	}

	public TreeJson(String id, String pid, String text, String iconCls,
			String state, boolean checked) {
		super();
		this.id = id;
		this.pid = pid;
		this.text = text;
		this.iconCls = iconCls;
		this.state = state;
		this.checked = checked;
	}

	private static void getChildrenNodes(List<TreeJson> parentnodes,
			TreeJson node) {
		// ѭ���������и��ڵ��node����ƥ�䣬ȷ�����ӹ�ϵ
		for (int i = parentnodes.size() - 1; i >= 0; i--) {

			TreeJson pnode = parentnodes.get(i);
			// ����Ǹ��ӹ�ϵ��Ϊ���ڵ������ӽڵ㣬�˳�forѭ��
			if (pnode.getId().equals(node.getPid())) {
				pnode.setState("closed");// �رն�����
				pnode.getChildren().add(node);
				return;
			} else {
				// ������Ǹ��ӹ�ϵ��ɾ�����ڵ�ջ�ﵱǰ�Ľڵ㣬
				// �����˴�ѭ����ֱ��ȷ�����ӹ�ϵ�򲻴����˳�forѭ��
				parentnodes.remove(i);
			}
		}
	}
}