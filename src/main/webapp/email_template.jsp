<%@page import="java.util.Date"%>
<%@ page language="java"  contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0" bgcolor="#f2f2f2">
	<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#f2f2f2">
		<tr>
			<td valign="top" align="center">
				<table width="551" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="left" bgcolor="#ffffff" style="BORDER-RIGHT: #9c9c9c 1px solid; PADDING-RIGHT: 5px; BORDER-TOP: #9c9c9c 1px solid; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; BORDER-LEFT: #9c9c9c 1px solid; PADDING-TOP: 5px; BORDER-BOTTOM: #9c9c9c 1px solid">
							<table>
								<tr>
									<td style="BORDER-TOP: #424242 7px solid; BORDER-LEFT: #424242 7px solid; BORDER-BOTTOM: #424242 7px solid;BORDER-RIGHT: #424242 7px solid;">
											<table width="525" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td style="PADDING-RIGHT: 20px; PADDING-LEFT: 20px; PADDING-BOTTOM: 20px; PADDING-TOP: 25px">
											<table width="485" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td><img src="${emailVo.appUrl}/images/wilmar_logo.jpg" alt="Yout Company Name" width="170" height="66"></td>
													<td align="right" valign="center" style="FONT-SIZE: 28px; COLOR: black; FONT-FAMILY: Arial, Helvetica, sans-serif">[益海嘉里]税务信息平台</td>
												</tr>
												<tr>
													<td colspan="2" align="right" valign="center" style="FONT-SIZE: 14px; COLOR: #acabab; FONT-FAMILY: Arial, Helvetica, sans-serif">${emailVo.sendDate}</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td bgcolor="#424242" style="BORDER-TOP: #d6d6d6 1px solid; PADDING-BOTTOM: 3px; PADDING-TOP: 3px; BORDER-BOTTOM: #d6d6d6 1px solid;height:39px">
										</td>
									</tr>
									
									<tr>
										<td style="PADDING-RIGHT: 20px; PADDING-LEFT: 20px; FONT-SIZE: 14px; PADDING-BOTTOM: 0px; COLOR: #333333; LINE-HEIGHT: 20px; PADDING-TOP: 30px; FONT-FAMILY: Arial, Helvetica, sans-serif;vertical-align:top;height: 300px">
											<p>尊敬的 <b>${emailVo.receiverName}</b>，</p>
											<fmt:setLocale value="zh_CN"/>
											<c:forEach var="noticeVo" items="${emailVo.noticeVos}">
												<c:if test="${noticeVo.subjectVo.display}">
													<p style="FONT-SIZE: 18px; MARGIN: 10px 0px 10px 0px; COLOR: #0165ab;">
														<A style="COLOR: #0165ab" href="${noticeVo.subjectVo.link}" target=_blank>${noticeVo.subjectVo.subject}</A>&nbsp;&nbsp;&nbsp;
														<img src="${noticeVo.subjectVo.important}">&nbsp;&nbsp;
														<img src="${noticeVo.subjectVo.urgent}" >&nbsp;&nbsp;
													</p>
												</c:if>
												<p style="margin:0px">
														<span lang="EN-US" style="font-size:10.5pt;font-family:Wingdings;mso-fareast-font-family:Wingdings;mso-bidi-font-family:Wingdings;color:#333333">
															<span style="mso-list:Ignore">l
																<span style="font:7.0pt &quot;Times New Roman&quot;">
																&nbsp;
		        												</span>
		        											</span>
		        										</span>
														<b><fmt:formatDate value="${noticeVo.notice.createdDatetime}" type="both" pattern="yyyy-MM-dd HH:mm" /></b>
														<b>${noticeVo.notice.content}</b>
												</p>
														<c:if test="${noticeVo.detailsDisplay}">
															<table style="font-size:10.0pt;width: 453px;border: solid 1px; margin-left:30px; background: #EAF1DD;">
																<tr>
																	<td>
																	<c:forEach var="detail" items="${noticeVo.noticeDetails}">
																		<c:if test="${detail.display}">
																			<p style="margin: 0px;text-indent: 16.05pt;word-break: break-all;">
																				<b>${detail.detailType}</b>${detail.value}
																			</p>
																		</c:if>
																	</c:forEach>
																	</td>
																</tr>
															</table>
														</c:if>
													</c:forEach>
										</td>
									</tr>
									<tr>
										<td style="PADDING-RIGHT: 0px; BORDER-TOP: #d6d6d6 1px solid; PADDING-LEFT: 20px; FONT-SIZE: 12px; PADDING-BOTTOM: 15px; COLOR: #666666; LINE-HEIGHT: 17px; PADDING-TOP: 15px; FONT-FAMILY: Arial, Helvetica, sans-serif; BACKGROUND-COLOR: #f6f6f6">
											<b>WCS - Wilmar Consultancy Services</b><br>
											上海市浦东新区峨山路91弄20号9号楼南塔6楼   邮编 ：200127<br> <!--
										<A style="COLOR: #4089bb" href="mailto:info@yourcompany.com" target=_blank >info@yourcompany.com</A><br>
										--> <a target="_blank" href="http://www.wcs-global.com/" style="COLOR: #4089bb">
												http://www.wcs-global.com/</a><br> <a target="_blank" href="http://tih.wilmar.cn" style="COLOR: #4089bb">
												http://tih.wilmar.cn/</a>
										</td>
									</tr>
								</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>