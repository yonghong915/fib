package com.fib.upp.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.XPath;

public enum XmlHelper {
	INSTANCE;

	public void unpack(Message msg, Element bisel, Element modul) {
//		if (null == templateEle) {
//			return;
//		}
//
//		List<Element> eleLst = templateEle.elements();
//		if (null == eleLst || eleLst.isEmpty()) {
//			return;
//		}

		for (int i = 0, len = modul.elements().size(); i < len; i++) {

			Element element = (Element) modul.elements().get(i);
			if (element.attributeValue("Ccy") != null) {
				// msg.setValue("货币符号", element.attributeValue("Ccy"));
			}
			if (element.elements().size() > 0) {

				if (element.attributeValue("Multi") != null && element.attributeValue("Multi").equals("true")) {
					String path = element.getPath();
					List<Element> list = getNodeList(path, bisel);
					if (list != null) {
						@SuppressWarnings("unused")
						int count = list.size();
						if (list.size() > 0) {
							for (int j = 0; j < list.size(); j++) {
								Element chirldel = (Element) list.get(j);
								dealMulti(msg, chirldel, element, j + 1, element.getPath());
							}
						}
					}

				} else {
					unpack(msg, bisel, element);
				}

			} else {
				if (element.attributeValue("Multi") != null && element.attributeValue("Multi").equals("true")) {
					String path = element.getPath();
					List<?> list = getNodeList(path, bisel);
					if (list != null) {
						List<String> entitys = null;
						if (list.size() > 0) {
							for (int j = 0; j < list.size(); j++) {
								Element chirldel = (Element) list.get(j);
								System.out.println("chirldel.getName()========" + chirldel.getName());
								String key = element.attributeValue("key");
								entitys = (List<String>) msg.getEntity(key);

//								boolean check = CommonCheck.check(msg, element.attributeValue("key"),
//										chirldel.getText(), element.attributeValue("len"),
//										element.attributeValue("must"), element.attributeValue("china"),
//										element.attributeValue("clsNm"), element.attributeValue("clen"));
//								if (!check) {
//									throw new ChannelCheckExp("检验失败");
//								}
								if (entitys != null) {
									entitys.add(chirldel.getText());
									msg.setEntity(key, entitys);
								} else {
									entitys = new ArrayList<String>();
									entitys.add(chirldel.getText());
									msg.setEntity(key, entitys);
								}

							}
						}
					}
				} else {
					String path = element.getPath();
					String value = getSingleNodeText(path, bisel);

					if (value != null && !value.trim().equals("")) {
						String matefield = element.attributeValue("matefield");
						if (matefield != null && matefield.equals("true")) {
							String str = element.attributeValue("key");
							String s[] = str.split("[;]");
							for (String key : s) {
								msg.setValue(key, value);
							}
						} else {
							msg.setValue(element.attributeValue("key"), value);
							System.out.println("===" + element.attributeValue("key") + ":" + value);
						}
					}
//					boolean check = CommonCheck.check(msg, element.attributeValue("key"), value,
//							element.attributeValue("len"), element.attributeValue("must"),
//							element.attributeValue("china"), element.attributeValue("clsNm"),
//							element.attributeValue("clen"));
//					if (!check) {
//						// msg.setValue("checkFlag", "false");
//						// return ;
//						throw new ChannelCheckExp("检验失败");
//					}
				}
			}

		}
	}

	/*
	 * 根据路径和Elment取得对应的路径下的值 author dailiming
	 * 
	 * @param path String 标签全路径
	 * 
	 * @param rootEl String 标签元素
	 * 
	 */
	public String getSingleNodeText(String path, Element rootEl) {
		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		nameSpaceMap.put("default", rootEl.getNamespaceURI());
		path = path.replaceAll("/", "/default:");

		// System.out.println(path);
		XPath xItemName = rootEl.createXPath(path);
		xItemName.setNamespaceURIs(nameSpaceMap);
		String returnstr = "";
		Element element = (Element) xItemName.selectSingleNode(rootEl);
		if (element != null) {
			returnstr = element.getText();
		}
		return returnstr;
	}

	/*
	 * 根据路径和Elment取得对应的路径下的元素集合 author dailiming
	 * 
	 * @param path String 标签全路径
	 * 
	 * @param rootEl String 标签元素
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getNodeList(String path, Element rootEl) {
		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		nameSpaceMap.put("default", rootEl.getNamespaceURI());
		path = path.replaceAll("/", "/default:");
		XPath xItemName = rootEl.createXPath(path);
		xItemName.setNamespaceURIs(nameSpaceMap);
		List<Element> list = new ArrayList<Element>();
		Object obj = xItemName.selectNodes(rootEl);
		if (obj instanceof List) {
			list = (List<Element>) obj;
		} else if (obj instanceof Element) {
			Element element = (Element) obj;
			list.add(element);
		}
		return list;
	}

	public String dealMessage(Message msg, Element root, Element modul) {

		if (modul != null) {

			if (modul.elements().size() > 0) {
				for (int i = 0; i < modul.elements().size(); i++) {
					Element element = (Element) modul.elements().get(i);
					if (element.getName().equalsIgnoreCase("NPCPrcInf")) {
						continue;
					}

					boolean cnyAtribute = false;
					if (element.attributeValue("Ccy") != null) {
						cnyAtribute = true;
					}

					if (element.elements().size() > 0) {
						if (element.attributeValue("Multi") != null && element.attributeValue("Multi").equals("true")) {
							String key = getKey(element);
							List<?> ls = (List<?>) msg.getEntity(key);
							if (ls != null) {
								for (int k = 0; k < ls.size(); k++) {
									Element el = root.addElement(element.getName());

									if (cnyAtribute) {
										el.addAttribute("Ccy",
												msg.getValue("货币符号").equals("") ? "CNY" : msg.getValue("货币符号"));
									}
									dealMulti(msg, el, element, k);
								}
							}

						} else {
							Element el = root.addElement(element.getName());
							if (cnyAtribute) {
								el.addAttribute("Ccy", msg.getValue("货币符号"));
							}
							dealMessage(msg, el, element);

						}
					} else {
						if (element.attributeValue("split") != null && element.attributeValue("split").equals("true")) {
							List<Element> list = (List<Element>) msg.getDetails("业务明细");
							if (list != null) {
								for (Element n : list) {
									root.add(n);
								}
							} else {
								list = (List<Element>) msg.getDetails("业务明细清单");
							}

						} else if (element.attributeValue("Multi") != null
								&& element.attributeValue("Multi").equals("true")) {

							if (element.elements().size() >= 0) {
								List<?> list = (List<?>) msg.getEntity(element.attributeValue("key"));
								if (list != null && list.size() > 0) {

									String value = "";
									for (int j = 0; j < list.size(); j++) {
										value = list.get(j).toString();
										if (value != null && !value.trim().equals("")) {
											Element emt = root.addElement(element.getName());
											// emt.setText(value);

											if (cnyAtribute) {
//												emt.addAttribute("Ccy",
//														msg.getValue("货币符号").equals("") ? "CNY" : msg.getValue("货币符号"));
//												emt.setText(StringUtil.moneyFormat(value));
											} else {
												emt.setText(value);
											}

										}

									}
								}
							}

						} else {
							if (element.attributeValue("key") != null && element.attributeValue("key").length() > 4
									&& element.attributeValue("key").substring(0, 4).equals("固定填写")) {
								Element chel = root.addElement(element.getName());
								chel.setText(element.attributeValue("key").substring(4));

							} else {
								if (!msg.getValue(element.attributeValue("key")).equals("")) {
									Element chel = root.addElement(element.getName());

									// chel.setText(msg.getValue(element.attributeValue("key")));

									if (cnyAtribute) {
										chel.addAttribute("Ccy",
												msg.getValue("货币符号").equals("") ? "CNY" : msg.getValue("货币符号"));
										// chel.setText(
										// StringUtil.moneyFormat(msg.getValue(element.attributeValue("key"))));
									} else {
										chel.setText(msg.getValue(element.attributeValue("key")));
									}

								}
//	    						
							}
						}
					}
				}
			}
		}
		return root.asXML();
	}

	public String getKey(Element el) {
		String str = "";
		if (el != null) {
			if (el.elements().size() > 0) {
				for (int i = 0; i < el.elements().size(); i++) {
					Element element = (Element) el.elements().get(i);
					if (element.elements().size() > 0) {
						str = getKey(element);
					} else {
						str = element.attributeValue("key");
						return str;
					}
				}
			}
		}
		return str;

	}

	/**
	 * 组包时对于重复标签的处理
	 * 
	 * @param msg
	 * @param root
	 * @param modul
	 * @param count
	 */
	public void dealMulti(Message msg, Element root, Element modul, int count) {
		if (modul != null && root != null) {
			if (modul.elements().size() > 0) {
				for (int i = 0; i < modul.elements().size(); i++) {
					Element element = (Element) modul.elements().get(i);
					if (element.elements().size() > 0) {
						Element el = root.addElement(element.getName());
						dealMulti(msg, el, element, count);
					} else {

						List<?> list = (List<?>) msg.getEntity(element.attributeValue("key"));
						if (list != null && list.get(count) != null && list.get(count).toString() != null
								&& !list.get(count).toString().trim().equals("")) {
							Element chel = root.addElement(element.getName());
							if (element.attributeValue("Ccy") != null) {
								chel.setAttributeValue("Ccy",
										msg.getValue("货币符号").equals("") ? "CNY" : msg.getValue("货币符号"));
							}

							chel.setText(list.get(count).toString());

						}

					}
				}
			}
		}
	}

	/*
	 * 处理多个重复标签的解析
	 * 
	 * @param msg Message
	 * 
	 * @param bisel Element 业务业务报文
	 * 
	 * @param modul Element 模板报文 author dailiming
	 */
	@SuppressWarnings("unchecked")
	public void dealMulti(Message msg, Element bisel, Element modul, int count, String conlpath) {
		if (modul != null) {
			if (modul.elements().size() > 0) {
				String unpath = conlpath + "[" + count + "]";
				// System.out.println("根路径为："+unpath);
				// System.out.println("相对路径为："+bisel.getUniquePath());
				Element el = null;
				String key = "";
				List<String> value = null;
				for (int i = 0; i < modul.elements().size(); i++) {
					Element element = (Element) modul.elements().get(i);
					// element.
					if (element.elements().size() > 0) {
						// el = (Element)bisel.selectSingleNode(element.getUniquePath());
						dealMulti(msg, bisel, element, count, conlpath);
					} else {
						key = element.attributeValue("key");
						String path = unpath + element.getUniquePath().substring(conlpath.length());
						// System.out.println("absolutly path="+path);
						el = getSingleNode(path, bisel);
						String bisvalue = "";

						if (el != null) {
							// System.out.println("找到了对应标签");
							bisvalue = el.getText();

//	    					boolean check=CommonCheck.check(msg,element.attributeValue("key"),el.getText(),element.attributeValue("len"),element.attributeValue("must"),element.attributeValue("china"),element.attributeValue("clsNm"),element.attributeValue("clen"));
//	    					if(!check){
//	    						//throw new ChannelCheckExp("检验失败");
//	    						return ;
//	    					}

							// System.out.println("循环体里面的值是" +bisvalue);
							value = (List<String>) msg.getEntity(key);
							if (value == null) {
								value = new ArrayList<String>();
								value.add(bisvalue);
								msg.setEntity(key, value);
							} else {
								value.add(bisvalue);
								msg.setEntity(key, value);
							}

						} else {
//							boolean check = CommonCheck.check(msg, element.attributeValue("key"), null,
//									element.attributeValue("len"), element.attributeValue("must"),
//									element.attributeValue("china"), element.attributeValue("clsNm"),
//									element.attributeValue("clen"));
//							if (!check) {
//								return;
//								// throw new ChannelCheckExp("检验失败");
//							}
							// System.out.println("没找到找到了对应标签");
							// System.out.println("循环体里面的值是空的");
							value = (List<String>) msg.getEntity(key);
							if (value == null) {
								value = new ArrayList<String>();
								value.add("");
								msg.setEntity(key, value);
							} else {
								value.add("");
								msg.setEntity(key, value);
							}

						}
					}
				}
			}
		}
	}

	public Element getSingleNode(String path, Element rootEl) {
		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		nameSpaceMap.put("default", rootEl.getNamespaceURI());
		path = path.replaceAll("/", "/default:");

		// System.out.println(path);
		XPath xItemName = rootEl.createXPath(path);
		xItemName.setNamespaceURIs(nameSpaceMap);
		Element element = (Element) xItemName.selectSingleNode(rootEl);
		return element;
	}
}
