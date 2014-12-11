(function($, AppConfig, LC) {
	if (!AppConfig) {
		return;
	}
	var UI = {};
	UI.EVENTS = {};
	(function(ev) {
		var hastouch;
		hastouch = ev.hastouch = ((window.DocumentTouch && document instanceof window.DocumentTouch) || "ontouchstart" in window);
		ev.touchstart = hastouch ? "touchstart" : "mousedown";
		ev.touchmove = hastouch ? "touchmove" : "mousemove";
		ev.touchend = hastouch ? "touchend" : "mouseup";
		ev.resize = "onorientationchange" in window ? "orientationchange"
				: "resize";
	})(UI.EVENTS);
	UI.Cookie = function(name, value, options) {
		if (typeof value != "undefined") {
			options = options || {
				"domain" : document.domain
			};
			if (value === null) {
				value = "";
				options.expires = -1;
			}
			var expires = "";
			if (options.expires
					&& (typeof options.expires == "number" || options.expires.toUTCString)) {
				var date;
				if (typeof options.expires == "number") {
					date = new Date;
					date.setTime(date.getTime() + options.expires * 1000);
				} else {
					date = options.expires;
				}
				expires = "; expires=" + date.toUTCString();
			}
			var path = options.path ? "; path=" + options.path : "";
			var domain = options.domain ? "; domain=" + options.domain : "";
			var secure = options.secure ? "; secure" : "";
			document.cookie = [ name, "=", escape(value), expires, path,
					domain, secure ].join("");
		} else {
			var cookieValue = null;
			if (document.cookie && document.cookie != "") {
				var cookies = document.cookie.split(";");
				for (var i = 0; i < cookies.length; i++) {
					var cookie = $.trim(cookies[i]);
					if (cookie.substring(0, name.length + 1) == name + "=") {
						cookieValue = unescape(cookie
								.substring(name.length + 1));
						break;
					}
				}
			}
			return cookieValue;
		}
	};
	UI.log = function(str) {
		var $log = $("#log");
		if (!$log.length) {
			return;
		}
		$log.append("<li>" + (new Date()).toUTCString() + "--" + str + "</li>");
	};
	/*
	 * pageVisibility.js
	 */
	UI.pageVisibility = (function() {
		var prefixSupport, keyWithPrefix = function(prefix, key) {
			if (prefix !== "") {
				return prefix + key.slice(0, 1).toUpperCase() + key.slice(1);
			}
			return key;
		};
		var isPageVisibilitySupport = (function() {
			var support = false;
			if (typeof window.screenX === "number") {
				[ "webkit", "moz", "ms", "o", "" ]
						.forEach(function(prefix) {
							if (support == false
									&& document[keyWithPrefix(prefix, "hidden")] != undefined) {
								prefixSupport = prefix;
								support = true;
							}
						});
			}
			return support;
		})();
		var isHidden = function() {
			if (isPageVisibilitySupport) {
				return document[keyWithPrefix(prefixSupport, "hidden")];
			}
			return undefined;
		};
		var visibilityState = function() {
			if (isPageVisibilitySupport) {
				return document[keyWithPrefix(prefixSupport, "visibilityState")];
			}
			return undefined;
		};
		return {
			isPageVisibilitySupport : isPageVisibilitySupport,
			isHidden : isHidden,
			visibilityState : visibilityState,
			visibilitychange : function(fn, usecapture) {
				usecapture = undefined || false;
				if (isPageVisibilitySupport && typeof fn === "function") {
					return document.addEventListener(prefixSupport
							+ "visibilitychange", function(evt) {
						this.hidden = isHidden();
						this.visibilityState = visibilityState();
						fn.call(this, evt);
					}.bind(this), usecapture);
				}
				return undefined;
			}
		};
	})(undefined);
	var ua, _AppConfig, downTimer, _BaseUrl, download_id, reportOpt, appOriStatus;
	appOriStatus = "uninstalled";
	_AppConfig = $.extend({}, AppConfig);
	_BaseUrl = LC.BaseUrl;
	ua = detectua(navigator.userAgent);
	reportOpt = {};
	!function() {
		var start, end, path = location.pathname;
		if (!(start = path.lastIndexOf("/"))) {
			start = 0;
		}
		start = path.lastIndexOf("/") + 1;
		if ((end = path.lastIndexOf(".")) <= start) {
			reportOpt.page = path.slice(start);
		} else {
			reportOpt.page = path.slice(start, end);
		}
	}();
	function detectua(ua) {
		var os = {}, browser = {}, android = ua
				.match(/([a|A]ndroid)[\s\/]*([\d.]+)/), ipad = ua
				.match(/(iPad).*OS\s([\d_]+)/), iphone = !ipad
				&& ua.match(/(iPhone\sOS)\s([\d_]+)/), wechat = ua
				.match(/MicroMessenger\/([\d\.]+)/), qqbrowser = ua
				.match(/MQQBrowser\/([\d\.]+)/);
		if (/android/i.test(ua)) {
			os.android = true;
			if (android) {
				os.version = android[2];
			}
		}
		if (/(iPhone|iPad|iPod|iOS)/g.test(ua)) {
			os.ios = true;
			if (iphone) {
				os.iphone = true, os.version = iphone[2].replace(/_/g, ".");
			}
			if (ipad) {
				os.ipad = true, os.version = ipad[2].replace(/_/g, ".");
			}
		}
		if (wechat) {
			browser.wechat = true, browser.version = wechat[1];
		}
		if (qqbrowser) {
			browser.qqbrowser = true, browser.version = qqbrowser[1];
		}
		return {
			os : os,
			browser : browser
		};
	}
	function errorMsg(othermsg, type) {
		var str = "";
		othermsg = othermsg || "异常，稍后刷新重试！";
		if ("undefined" != typeof type) {
			switch (type) {
			case 0:
				str += "运行异常 type:" + type + ";msg:" + othermsg;
				break;
			case 1:
				str += "运行出错 type:" + type + ";msg:" + othermsg;
				break;
			default:
				str += "type:" + type + ";msg:" + othermsg;
			}
		} else {
			str = othermsg;
		}
		alert(str);
	}
	function report(type, id, params) {
		var url = _BaseUrl + "/ajax/log/ActionLog?page=" + reportOpt.page
				+ "&type=" + type + "&id=" + id + "&appid=" + AppConfig.appId;
		if (params) {
			var key, pStr = "";
			for (key in params) {
				pStr += "&" + encodeURIComponent(key) + "="
						+ encodeURIComponent(params[key]);
			}
			url += pStr;
		}
		url += "&r=" + Math.random();
		//$.get(url, function(data, status) {
			
		//});
	}
	function getAppUrl(type) {
		var str;
		if (type == 1) {
			str = "";
			if (ua.os.android) {
				if (ua.browser.wechat) {
					str = "hostpname=com.tencent.mm&";
				} else {
					if (ua.browser.qqbrowser) {
						str = "hostpname=com.tencent.mtt&";
					}
				}
			}
			str = str
					+ "pname="
					+ _AppConfig.packageName
					+ (_AppConfig.appChannel ? ("&channelid=" + _AppConfig.appChannel)
							: "")
					+ (_AppConfig.appId ? ("&appid=" + _AppConfig.appId) : "")
					+ "&oplist=1;2";
			str = "tmast://appdetails?" + str;
		} else {
			if (type == 2) {
				str = _BaseUrl
						+ "/down/"
						+ _AppConfig.packageName
						+ (_AppConfig.appChannel ? ("?channel=" + _AppConfig.appChannel)
								: "");
			} else {
				if (type == 3) {
					str = _AppConfig.iosUrl;
				}
			}
		}
		return str;
	}
	function getCommData(durTime) {
		durTime = durTime || 5 * 60 * 1000;
		var starttime = (new Date()).getTime();
		var endtime = starttime + durTime;
		var url = getAppUrl(1);
		alert(url);
		var data = {
			starttime : starttime,
			endtime : endtime,
			url : url
		};
		return JSON.stringify(data);
	}
	function initAppbaoBanner() {
		var $closebtn = $("#appbao-download-banner .btn-close"), $downbtn = $("#appbao-download-banner .btn-appbao-download");
		$downbtn.bind(UI.EVENTS.touchstart, function(ev) {
			var url, load, $btn = $(ev.target);
			load = $btn.attr("data-load");
			if (load != "1") {
				$btn.addClass("btn-unactive").attr("data-load", "1");
				url = $btn.attr("data-url");
				setTimeout(function() {
					$btn.removeClass("btn-unactive").attr("data-load", "0");
				}, 500);
				if (url) {
					location.href = url;
				}
				report(0, 602);
			}
			ev.preventDefault();
			ev.stopPropagation();
		});
		$closebtn.bind("click", function(ev) {
			$("#appbao-download-banner").hide();
			report(0, 603);
			ev.preventDefault();
			ev.stopPropagation();
		});
	}
	function bindFastDownLoad(type) {
		var fn, btnStr, ft;
		btnStr = "高速下载";
		if (type == 1) {
			btnStr = "高速下载";
			fn = function() {
				report(0, ua.browser.wechat ? 606 : 600);
				if (!_AppConfig.iosUrl) {
					alert("抱歉，当前不支持IOS版本的下载！");
				} else {
					location.href = _AppConfig.iosUrl;
				}
			};
		} else {
			if (type == 2) {
				btnStr = (appOriStatus.indexOf("update") > -1) ? "高速更新"
						: "高速下载";
				fn = function() {
					report(0, ua.browser.wechat ? 606 : 600);
					location.href = getAppUrl(2);
				};
			} else {
				if (type == 3) {
					btnStr = (appOriStatus.indexOf("update") > -1) ? "高速更新"
							: "高速下载";
					fn = function() {
						report(0, ua.browser.wechat ? 606 : 600);
						location.href = getAppUrl(1);
					};
				} else {
					if (type == 4) {
						btnStr = (appOriStatus.indexOf("update") > -1) ? "高速更新"
								: "高速下载";
						fn = function() {
							report(0, ua.browser.wechat ? 606 : 600);
							var commdata = getCommData();
							if (download_id) {
								checkAppbao(function() {
									bindFastDownLoad(3);
									location.href = getAppUrl(1);
								}, function() {
									installAppbao(download_id, function() {
										checkAppbao(function() {
											bindFastDownLoad(3);
										}, function() {
											bindFastDownLoad(4);
										});
									}, function() {
										bindFastDownLoad(5);
									});
								});
							} else {
								bindFastDownLoad(5);
							}
						};
					} else {
						if (type == 5) {
							btnStr = (appOriStatus.indexOf("update") > -1) ? "高速更新"
									: "高速下载";
							fn = function() {
								report(0, ua.browser.wechat ? 606 : 600);
								var commdata = getCommData();
								try {
									WeixinJSBridge
											.invoke(
													"writeCommData",
													{
														"packageName" : _AppConfig.appbaoPkg,
														"data" : commdata
													},
													function(res) {
														var err_msg = $
																.trim(res.err_msg);
														if (err_msg == "write_comm_data:ok") {
															WeixinJSBridge
																	.invoke(
																			"addDownloadTask",
																			{
																				"task_name" : _AppConfig.appbaoName,
																				"task_url" : _AppConfig.appbaoUrl,
																				"file_md5" : _AppConfig.appbaoMd5
																			},
																			function(
																					res) {
																				download_id = res.download_id;
																				if (!download_id) {
																					download_id = "";
																					UI
																							.Cookie(
																									"download_id",
																									"",
																									{
																										expires : -1000
																									});
																					errorMsg("添加下载任务异常,稍后刷新重试");
																					bindFastDownLoad(2);
																					return;
																				}
																				UI
																						.Cookie(
																								"download_id",
																								"",
																								{
																									expires : -1000
																								});
																				UI
																						.Cookie(
																								"download_id",
																								download_id,
																								{
																									expires : 60 * 60 * 24
																								});
																				WeixinJSBridge
																						.on(
																								"wxdownload:state_change",
																								function(
																										res) {
																									var state;
																									if (download_id == res.download_id) {
																										state = $
																												.trim(res.state);
																										if (state == "download_succ") {
																											checkAppbao(
																													function() {
																														var str = getAppUrl(1);
																														bindFastDownLoad(3);
																														location.href = str;
																													},
																													function() {
																														var commdata = getCommData();
																														installAppbao(
																																download_id,
																																function() {
																																	checkAppbao(
																																			function() {
																																				bindFastDownLoad(3);
																																			},
																																			function() {
																																				bindFastDownLoad(4);
																																			});
																																},
																																function() {
																																	bindFastDownLoad(4);
																																});
																													});
																										} else {
																											if (state == "downloading") {
																											} else {
																												if (state == "download_fail"
																														|| state == "default") {
																													download_id = "";
																													UI
																															.Cookie(
																																	"download_id",
																																	"",
																																	{
																																		expires : -1000
																																	});
																													bindFastDownLoad(5);
																												}
																											}
																										}
																									}
																								});
																				bindFastDownLoad(6);
																			});
														} else {
															bindFastDownLoad(5);
														}
													});
								} catch (e) {
									errorMsg("异常，稍后刷新重试！");
									bindFastDownLoad(2);
								}
							};
						} else {
							if (type == 6) {
								btnStr = "下载中";
								fn = function() {
									alert("下载中，亲请稍后~");
								};
							} else {
								if (type == 7) {
									btnStr = (appOriStatus.indexOf("update") > -1) ? "高速更新"
											: "高速下载";
									fn = function() {
										report(0, ua.browser.wechat ? 606 : 600);
										var starttime, timer;
										if ($(".btn-fast-download").hasClass(
												"btn-unactive")) {
											return;
										}
										$(".btn-fast-download").addClass(
												"btn-unactive").text("下载中");
										starttime = new Date();
										timer = setInterval(
												function() {
													clearInterval(timer);
													var now;
													now = new Date();
													if (parseInt(now.getTime())
															- parseInt(starttime
																	.getTime()) < 3000) {
														var isHidden = UI.pageVisibility
																.isHidden();
														if (!isHidden) {
															location.href = getAppUrl(2);
														}
													}
													$(".btn-fast-download")
															.removeClass(
																	"btn-unactive")
															.text(btnStr);
												}, 2000);
										tryCallAppbao(getAppUrl(1));
									};
								} else {
									if (type == 8) {
										btnStr = "打开";
										fn = function() {
											report(0, ua.browser.wechat ? 606
													: 600);
											if (_AppConfig.sigMd5) {
												try {
													WeixinJSBridge
															.invoke(
																	"launch3rdApp",
																	{
																		"packageName" : _AppConfig.packageName,
																		"signature" : _AppConfig.sigMd5,
																		"type" : 1
																	},
																	function(
																			res) {
																	});
												} catch (e) {
													errorMsg("异常，稍后刷新重试！");
													bindFastDownLoad(2);
												}
											} else {
												alert("抱歉，该应用暂不支持微信打开，您可自行手动打开！");
											}
										};
									} else {
										if (type == 9) {
											btnStr = "打开";
											fn = function() {
												report(0,
														ua.browser.wechat ? 606
																: 600);
												try {
													window.x5mtt
															.packages()
															.runApk(
																	JSON
																			.stringify({
																				packagename : _AppConfig.packageName
																			}));
												} catch (e) {
													errorMsg("异常，稍后刷新重试！");
													bindFastDownLoad(2);
												}
											};
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (type == 6) {
			$(".btn-fast-download").addClass("btn-loading").text(btnStr)
					.unbind(UI.EVENTS.touchstart)
					.bind(UI.EVENTS.touchstart, fn);
		} else {
			ft = function() {
				var timer;
				if (type != 7) {
					if ($(".btn-fast-download").hasClass("btn-unactive")) {
						return;
					}
					$(".btn-fast-download").addClass("btn-unactive");
					timer = setInterval(function() {
						clearInterval(timer);
						$(".btn-fast-download").removeClass("btn-unactive");
					}, 500);
				}
				fn();
			};
			$(".btn-fast-download").removeClass("btn-loading").removeClass(
					"btn-unactive").text(btnStr).unbind(UI.EVENTS.touchstart)
					.bind(UI.EVENTS.touchstart, ft);
		}
	}
	function iosDownload() {
		var evt, $btn;
		bindFastDownLoad(1);
		report(0, ua.browser.wechat ? 606 : 600);
		if (!_AppConfig.iosUrl) {
			return;
		}
		if (ua.browser.wechat) {
			setTimeout(function() {
				location.href = _AppConfig.iosUrl;
			}, 1000);
		} else {
			location.href = _AppConfig.iosUrl;
		}
	}
	function browserDownload() {
		report(1, 601);
		$("#appbao-download-banner").show();
		if (UI.pageVisibility.isPageVisibilitySupport) {
			bindFastDownLoad(7);
		} else {
			bindFastDownLoad(2);
		}
	}
	function tryCallAppbao(url) {
		var $brige = $("#callAppbao-bridge");
		if (!url) {
			return;
		}
		if ($brige.length > 0) {
			$brige.remove();
			tryCallAppbao(url);
		} else {
			$("body")
					.append(
							'<iframe id="callAppbao-bridge" style="display:none;"width=0 height=0 frameborder=0 scrolling=auto src="'
									+ url + '"></iframe>');
		}
	}
	var _AppbaoBaseVersion = 1050125;
	function checkAppbao(yesCallback, noCallback) {
		try {
			WeixinJSBridge.invoke("getInstallState", {
				"packageName" : _AppConfig.appbaoPkg
			}, function(res) {
				var appbao, err_msg = $.trim(res.err_msg);
				if (err_msg == "missing arguments") {
					errorMsg("异常，稍后刷新重试！");
					return;
				}
				appbao = err_msg.match(/get_install_state:yes_([\d]+)/);
				if (appbao && parseInt(appbao[1]) >= _AppbaoBaseVersion) {
					("function" == typeof yesCallback) ? yesCallback(appbao[1])
							: "";
				} else {
					("function" == typeof noCallback) ? noCallback() : "";
				}
			});
		} catch (e) {
			errorMsg("异常，稍后刷新重试！");
		}
	}
	function installAppbao(l_download_id, successCallback, failCallback) {
		var commdata = getCommData();
		if (!l_download_id) {
			errorMsg("异常，稍后刷新重试！");
			return;
		}
		try {
			WeixinJSBridge.invoke("writeCommData", {
				"packageName" : _AppConfig.appbaoPkg,
				"data" : commdata
			}, function(res) {
				var err_msg = $.trim(res.err_msg);
				if (err_msg == "write_comm_data:ok") {
					WeixinJSBridge.invoke("installDownloadTask", {
						"download_id" : l_download_id
					}, function(res) {
						var err_msg = $.trim(res.err_msg);
						if (err_msg == "install_download_task:ok") {
							if ("function" == typeof successCallback) {
								successCallback();
							}
						} else {
							if ("function" == typeof failCallback) {
								failCallback();
							}
						}
					});
				} else {
					if ("function" == typeof failCallback) {
						failCallback();
					}
				}
			});
		} catch (e) {
			errorMsg("异常，稍后刷新重试");
		}
	}
	function wechatDownload() {
		try {
			WeixinJSBridge
					.invoke(
							"getInstallState",
							{
								"packageName" : _AppConfig.packageName
							},
							function(res) {
								var app, version, err_msg = $.trim(res.err_msg);
								if (err_msg == "missing arguments") {
									errorMsg("异常，稍后刷新重试");
									return;
								}
								app = /get_install_state:yes_([\d]+)/
										.exec(err_msg);
								if (app) {
									version = parseInt(app[1]);
									appOriStatus = (_AppConfig.versionCode > version) ? "update"
											: "installed";
								} else {
									appOriStatus = "uninstalled";
								}
								if (app
										&& appOriStatus.indexOf("installed") > -1
										&& _AppConfig.sigMd5) {
									bindFastDownLoad(8);
								} else {
									checkAppbao(
											function(ver) {
												bindFastDownLoad(3);
											},
											function() {
												download_id = UI
														.Cookie("download_id");
												if (download_id) {
													WeixinJSBridge
															.invoke(
																	"queryDownloadTask",
																	{
																		"download_id" : download_id
																	},
																	function(
																			res) {
																		var state = $
																				.trim(res.state);
																		if (state == "download_succ") {
																			checkAppbao(
																					function() {
																						bindFastDownLoad(3);
																					},
																					function() {
																						bindFastDownLoad(4);
																					});
																		} else {
																			if (state == "downloading") {
																				bindFastDownLoad(6);
																				var commdata = getCommData();
																				try {
																					WeixinJSBridge
																							.invoke(
																									"writeCommData",
																									{
																										"packageName" : _AppConfig.appbaoPkg,
																										"data" : commdata
																									},
																									function(
																											res) {
																										var err_msg = $
																												.trim(res.err_msg);
																										if (err_msg == "write_comm_data:ok") {
																										}
																									});
																					WeixinJSBridge
																							.on(
																									"wxdownload:state_change",
																									function(
																											res) {
																										var state;
																										if (download_id == res.download_id) {
																											state = $
																													.trim(res.state);
																											if (state == "download_succ") {
																												checkAppbao(
																														function() {
																															var str = getAppUrl(1);
																															bindFastDownLoad(3);
																															location.href = str;
																														},
																														function() {
																															installAppbao(
																																	download_id,
																																	function() {
																																		checkAppbao(
																																				function() {
																																					bindFastDownLoad(3);
																																				},
																																				function() {
																																					bindFastDownLoad(4);
																																				});
																																	},
																																	function() {
																																		bindFastDownLoad(4);
																																	});
																														});
																											} else {
																												if (state == "downloading") {
																													bindFastDownLoad(6);
																												} else {
																													if (state == "download_fail"
																															|| state == "default") {
																														download_id = "";
																														UI
																																.Cookie(
																																		"download_id",
																																		"",
																																		{
																																			expires : -1000
																																		});
																														bindFastDownLoad(5);
																													}
																												}
																											}
																										}
																									});
																					bindFastDownLoad(6);
																				} catch (e) {
																					errorMsg("异常，稍后刷新重试");
																				}
																			} else {
																				if (state == "download_fail"
																						|| state == "default") {
																					download_id = "";
																					UI
																							.Cookie(
																									"download_id",
																									"",
																									{
																										expires : -1000
																									});
																					bindFastDownLoad(5);
																				}
																			}
																		}
																	});
												} else {
													bindFastDownLoad(5);
												}
											});
								}
							});
		} catch (e) {
			errorMsg("异常，稍后刷新重试");
			bindFastDownLoad(2);
		}
	}
	function qqbrowserDownload() {
		var packages, appinfo;
		appOriStatus = "uninstalled";
		try {
			packages = window.x5mtt.packages();
		} catch (e) {
			packages = null;
		}
		if (!packages) {
			report(1, 601);
			$("#appbao-download-banner").show();
			bindFastDownLoad(2);
			return;
		}
		if (packages.isApkInstalled(JSON.stringify({
			"packagename" : _AppConfig.packageName
		})) == 1) {
			try {
				appinfo = JSON.parse(packages
						.getSingleApp(_AppConfig.packageName));
			} catch (e) {
				appinfo = null;
			}
			if (appinfo
					&& appinfo.versioncode
					&& parseInt(appinfo.versioncode) < parseInt(_AppConfig.versionCode)) {
				appOriStatus = "update";
			} else {
				appOriStatus = "installed";
			}
		} else {
			appOriStatus = "uninstalled";
		}
		if (appOriStatus == "installed") {
			report(1, 601);
			$("#appbao-download-banner").show();
			bindFastDownLoad(9);
		} else {
			if (packages.isApkInstalled(JSON.stringify({
				"packagename" : _AppConfig.appbaoPkg
			})) == 1) {
				bindFastDownLoad(3);
			} else {
				report(1, 601);
				$("#appbao-download-banner").show();
				bindFastDownLoad(2);
			}
		}
	}
	function init() {
		if ($("#appbao-download-banner").length) {
			initAppbaoBanner();
			$("#appbao-download-banner").hide();
		}
		if (ua.os.ios) {
			report(1, 605);
			iosDownload();
		} else {
			if (ua.os.android) {
				if (ua.browser.wechat) {
					report(1, 604);
				} else {
					report(1, 605);
				}
				if (ua.browser.wechat && ua.browser.version
						&& parseFloat(ua.browser.version) >= 5.1) {
					if (typeof WeixinJSBridge == "object"
							&& typeof WeixinJSBridge.invoke == "function") {
						wechatDownload();
					} else {
						if (document.addEventListener) {
							document.addEventListener("WeixinJSBridgeReady",
									wechatDownload, false);
						} else {
							if (document.attachEvent) {
								document.attachEvent("WeixinJSBridgeReady",
										wechatDownload);
								document.attachEvent("onWeixinJSBridgeReady",
										wechatDownload);
							}
						}
					}
				} else {
					if (ua.browser.qqbrowser && ua.browser.version
							&& parseFloat(ua.browser.version) >= 5) {
						qqbrowserDownload();
					} else {
						browserDownload();
					}
				}
			} else {
				report(1, 605);
				browserDownload();
			}
		}
	}
	$(function() {
		init();
	});
})($, window.APPCONFIG, window.LC);