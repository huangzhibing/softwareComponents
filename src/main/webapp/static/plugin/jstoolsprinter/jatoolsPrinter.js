function checkJatoolsPrinterInstalled() {
	var k = false, i = null, n = ["MSIE", "Firefox", "Chrome"];
	for (var l = 0; l < n.length; l++) {
		if (navigator.userAgent.indexOf(n[l]) > -1) {
			k = true;
			break
		}
	}
	if (!k) {
		i = "打印控件不支持本浏览器!"
	} else {
		/**
		 * 	if (navigator.userAgent.indexOf("Chrome") > -1) {
			var a = navigator.plugins, m = false;
			for (var f = 0; f < a.length; f++) {
				if (a[f].name.indexOf("jatoolsPrinter") == 0) {
					m = true;
					break
				}
			}
			if (!m) {
				i = "杰表打印控件未安装，请点击<a href='jatoolsPrinter.crx'>此处</a>安装."
			}
		}
		 */
	
	}
	if (i) {
		showError(i)
	}
}
function showError(g) {
	var h = document.getElementsByTagName("input");
	for (var e = 0; e < h.length; e++) {
		h[e].disabled = true
	}
	var f = document.getElementById("errs");
	f.innerHTML = g;
	f.style.display = "block"
}
function viewSource() {
	var b = document.URL.replace(/^http[s]?\:\/\/.*?\//i, "");
	window.showModalDialog("/sourceviewer/view.jsp?from=" + escape(b), null, "dialogWidth=1024px;dialogHeight=670px;status=no;help=no;scroll=no;resizable=yes")
}
function jpExit() {
	getJP().exit()
}
function JP(p) {
	function r(b, a) {
		return b.getElementById(a)
	}
	function v(d) {
		var b = "<style>";
		var e = d.styleSheets;
		for (var g = 0; g < e.length; g++) {
			var h = e[g];
			try {
				var c = h.cssRules;
				if (c) {
					for (var a = 0; a < c.length; a++) {
						b += c[a].cssText || ""
					}
				} else {
					if (h.cssText) {
						b += h.cssText
					}
				}
			} catch (f) {
			}
		}
		return b + "</style>"
	}
	function n(a, b) {
		if (a.doctype) {
			b.setAttribute("_strict", "true")
		}
		return b.outerHTML || (function(c) {
			var d = a.createElement("div"), e;
			d.appendChild(c.cloneNode(true));
			e = d.innerHTML;
			d = null;
			return e
		})(b)
	}
	function m(d, c) {
		if (typeof(d.getElementById) != "undefined") {
			var b = "NSAPI://" + v(d) + "--\n\n\n--";
			if (c.pages) {
				for (var a = 0; a < c.pages.length; a++) {
					var e = c.pages[a];
					if (typeof(e.substring) != "undefined") {
						e = r(d, e)
					}
					b += ("<div id='page" + (a + 1) + "'>" + n(d, e) + "</div>")
				}
			} else {
				var a = 0;
				while (true) {
					var e = r(d, (c.pagePrefix || "") + "page" + (a + 1));
					if (!e) {
						break
					}
					b += n(d, e);
					a++
				}
			}
			return b
		} else {
			if (d.html && d.all) {
				return d
			} else {
				if (d.html) {
					var b = "NSAPI:// --\n\n\n--";
					if (!d.html.push) {
						d.html = [d.html]
					}
					for (var a = 0; a < d.html.length; a++) {
						b += ("<div id='page" + (a + 1) + "'>" + d.html[a] + "</div>")
					}
					return b
				} else {
					return d
				}
			}
		}
	}
	function u(c) {
		var a = ["border", "border-radius", "box-shadow", "height", "margin", "padding", "width", "max-width", "min-width", "border-collapse", "border-spacing", "caption-side",
				"empty-cells", "table-layout", "direction", "font", "font-family", "font-style", "font-variant", "font-size", "font-weight", "letter-spacing", "line-height",
				"text-align", "text-decoration", "text-indent", "text-overflow", "text-shadow", "text-transform", "white-space", "word-spacing", "word-wrap", "vertical-align",
				"color", "background", "background-color", "background-image", "background-position", "background-repeat", "Opacity", "bottom", "clear", "clip", "cursor",
				"display", "float", "left", "opacity", "outline ", "overflow", "position", "resize ", "right", "top", "visibility", "z-index", "list-style-image",
				"list-style-position", "list-style-type"];
		var g = c.getElementsByTagName("*");
		for (var d = 0; d < g.length; d++) {
			var h = g.item(d);
			if (h.tagName == "IMG") {
				h.src = h.src
			}
			for (var e = 0; e < a.length; e++) {
				var f = a[e];
				var b = null;
				if (h.currentStyle) {
					b = h.currentStyle[f]
				} else {
					if (window.getComputedStyle) {
						if (window.getComputedStyle.getPropertyValue) {
							b = window.getComputedStyle(h, null).getPropertyValue(f)
						} else {
							b = window.getComputedStyle(h)[f]
						}
					}
				}
				if (b) {
					h.style[f] = b
				}
			}
		}
	}
	function t(a, b) {
		if (!b) {
			a.documents = o(a)
		}
		if (a.footer && a.footer.html.innerHTML) {
			u(a.footer.html);
			a.footer.html = a.footer.html.innerHTML
		}
		if (a.header && a.header.html.innerHTML) {
			u(a.header.html);
			a.header.html = a.header.html.innerHTML
		}
		return a
	}
	function o(c) {
		var d = c.documents, b = null;
		if (typeof(d.push) != "undefined") {
			b = [];
			for (var a = 0; a < d.length; a++) {
				b.push(m(d[a], c))
			}
			return b
		} else {
			return m(d, c)
		}
	}
	function l(a) {
		var b = "<html><head><style>" + v(a.ownerDocument) + "</style></head><body>" + n(a.ownerDocument, a) + "</body></html>";
		return b
	}
	function s(a) {
		var b = "<html><head><base href='" + a.URL + "'/><style>" + v(a) + "</style></head><body>" + a.body.innerHTML + "</body></html>";
		return b
	}
	function q() {
		var a = document.getElementById("ojatoolsPrinter");
		if (a) {
			return a.getAttribute("license") || ""
		}
		return ""
	}
	return ({
		proxy : p || null,
		crx : !p,
		activex : p && p.tagName == "OBJECT",
		npapi : p && p.tagName == "EMBED",
		extension : "jpnmbckmknckdkijflpiigdmfedhglnl",
		callbacks : [],
		eventIndex : 0,
		initialize : function() {
			var a = this;
			if (!p) {
				this.sendMessage({
							method : "new",
							lic : q()
						});
				window.addEventListener("message", function(b) {
							a.callbacks[b.data.event].apply(null, b.data.params || [b.data.data])
						})
			}
			return this
		},
		isInstalled : function(a) {
			var b = false;
			alert(this.proxy);
			if (!this.proxy) {
				a(typeof this.proxy.printPreview != "undefined");
			} else {
				this.sendMessage({
							method : "isInstalled"
						}, a)
			}
		},
		registerCallback : function(a) {
			if (a) {
				var b = "event-" + this.eventIndex++;
				this.callbacks[b] = a;
				return b
			} else {
				return ""
			}
		},
		emptyCallback : function() {
		},
		sendMessage : function(b, a) {
			chrome.runtime.sendMessage(this.extension, b, a || this.emptyCallback)
		},
		about : function() {
			this.proxy ? this.proxy.about() : this.sendMessage({
						method : "about"
					})
		},
		exit : function() {
			this.sendMessage({
						method : "exit"
					});
			alert("exit")
		},
		getPrinters : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getPrinters())
			} else {
				this.sendMessage({
							method : "getPrinters",
							event : this.registerCallback(a)
						})
			}
		},
		getPapers : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getPapers(b))
			} else {
				this.sendMessage({
							method : "getPapers",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		isCustomPaperSupported : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.isCustomPaperSupported(b))
			} else {
				this.sendMessage({
							method : "isCustomPaperSupported",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		registerMyDocListeners : function(c) {
			var a = ["done", "onState", "listener", "onPagePrinted"];
			for (var b = 0; b < a.length; b++) {
				var d = a[b];
				if (c[d]) {
					c[d] = this.registerCallback(c[d]);
					c._hasCallback = true
				}
			}
			if (c.dragDesigner && c.dragDesigner.ok) {
				c.dragDesigner.ok = this.registerCallback(c.dragDesigner.ok);
				c._hasCallback = true
			}
		},
		printPreview : function(c, b) {
			if (this.proxy) {
				if (this.proxy.tagName == "OBJECT") {
					this.proxy.printPreview(c, b ? true : false);
					return;
					var a = this;
					___loadDocuments(c, function() {
								a.proxy.printPreview(c, b ? true : false)
							})
				} else {
					this.proxy.printPreview(c, b ? true : false)
				}
			} else {
				c = t(c);
				this.registerMyDocListeners(c);
				this.sendMessage({
							method : "printPreview",
							params : [c, b ? true : false]
						})
			}
		},
		print : function(c, b) {
			c = t(c, this.activex);
			if (this.proxy) {
				if (this.proxy.tagName == "OBJECT") {
					this.proxy.print(c, b ? true : false);
					return;
					var a = this;
					___loadDocuments(c, function() {
								a.proxy.print(c, b ? true : false)
							})
				} else {
					this.proxy.print(c, b ? true : false)
				}
			} else {
				this.registerMyDocListeners(c);
				this.sendMessage({
							method : "print",
							params : [c, b ? true : false]
						})
			}
		},
		isExcelInstalled : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.isExcelInstalled())
			} else {
				this.sendMessage({
							method : "isExcelInstalled",
							event : this.registerCallback(a)
						})
			}
		},
		getDefaultPrinter : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getDefaultPrinter())
			} else {
				this.sendMessage({
							method : "getDefaultPrinter",
							event : this.registerCallback(a)
						})
			}
		},
		
		getVersion : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getVersion())
			} else {
				this.sendMessage({
							method : "getVersion",
							event : this.registerCallback(a)
						})
			}
		},
		isImplemented : function(a, b) {
			if (this.proxy) {
				(b || this.nothing).call(this, this.proxy.isImplemented(a))
			} else {
				this.sendMessage({
							method : "isImplemented",
							params : [a],
							event : this.registerCallback(b)
						})
			}
		},
		getLocalMacAddress : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getLocalMacAddress())
			} else {
				this.sendMessage({
							method : "getLocalMacAddress",
							event : this.registerCallback(a)
						})
			}
		},
		getCPUSerialNo : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getCPUSerialNo())
			} else {
				this.sendMessage({
							method : "getCPUSerialNo",
							event : this.registerCallback(a)
						})
			}
		},
		setOffsetPage : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.setOffsetPage(b))
			} else {
				this.sendMessage({
							method : "setOffsetPage",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		isPrintableFileType : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.isPrintableFileType(b))
			} else {
				this.sendMessage({
							method : "isPrintableFileType",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		setDragCSS : function(a, b, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.setDragCSS(a, b))
			} else {
				this.sendMessage({
							method : "setDragCSS",
							params : [a, b],
							event : this.registerCallback(c)
						})
			}
		},
		clearLastSettings : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.clearLastSettings(b))
			} else {
				this.sendMessage({
							method : "clearLastSettings",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		printTIFF : function(b, d, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.printTIFF(b, d, a))
			} else {
				this.sendMessage({
							method : "printTIFF",
							params : [b, d, a],
							event : this.registerCallback(c)
						})
			}
		},
		printDocument : function(b, a, c) {
			!a && (a = {});
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.printDocument(b, a))
			} else {
				this.sendMessage({
							method : "printDocument",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		exportAsImage : function(a, e, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.exportAsImage(a, e))
			} else {
				var d = a.id;
				var g = a.ownerDocument;
				var f = a.id = "tmp" + new Date().getTime();
				var b = s(g);
				a.id = d;
				this.sendMessage({
							method : "exportAsImage",
							params : [{
										html : b,
										element : f
									}, e],
							event : this.registerCallback(c)
						})
			}
		},
		exportAsExcel : function(b, e, d, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.exportAsExcel(b, e, d))
			} else {
				var a = l(b);
				this.sendMessage({
							method : "exportAsExcel",
							params : [a, e, d],
							event : this.registerCallback(c)
						})
			}
		},
		setupNormalOffset : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.setupNormalOffset(b))
			} else {
				this.sendMessage({
							method : "setupNormalOffset",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		download : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.download(b, a))
			} else {
				this.sendMessage({
							method : "download",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		printToImage : function(a, b, c) {
			a = t(a, this.activex);
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.printToImage(a, b))
			} else {
				this.registerMyDocListeners(a);
				this.sendMessage({
							method : "printToImage",
							params : [a, b],
							event : this.registerCallback(c)
						})
			}
		},
		printToPDF : function(a, b, c) {
			a = t(a, this.activex);
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.printToPDF(a, b))
			} else {
				this.registerMyDocListeners(a);
				this.sendMessage({
							method : "printToPDF",
							params : [a, b],
							event : this.registerCallback(c)
						})
			}
		},
		liveUpdate : function(a, b, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.liveUpdate(a, b))
			} else {
				this.sendMessage({
							method : "liveUpdate",
							params : [a, b],
							event : this.registerCallback(c)
						})
			}
		},
		getFonts : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getFonts())
			} else {
				this.sendMessage({
							method : "getFonts",
							event : this.registerCallback(a)
						})
			}
		},
		copy : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.copy(b, a))
			} else {
				this.sendMessage({
							method : "copy",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		copied : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.copied(b || ""))
			} else {
				this.sendMessage({
							method : "copied",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		getServerUID : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getServerUID(b || ""))
			} else {
				this.sendMessage({
							method : "getServerUID",
							params : [b],
							event : this.registerCallback(a)
						})
			}

		},
		writeString : function(b, a, d, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.writeString(b, a, d))
			} else {
				this.sendMessage({
							method : "writeString",
							params : [b, a, d],
							event : this.registerCallback(c)
						})
			}
		},
		writeBase64 : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.writeBase64(b, a))
			} else {
				this.sendMessage({
							method : "writeBase64",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		readString : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.readString(b, a))
			} else {
				this.sendMessage({
							method : "readString",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		readBase64 : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.readBase64(b))
			} else {
				this.sendMessage({
							method : "readBase64",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		readHTML : function(a, b, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.readHTML(a, b))
			} else {
				this.sendMessage({
							method : "readHTML",
							params : [a, b],
							event : this.registerCallback(c)
						})
			}
		},
		chooseFile : function(a, d, b, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.chooseFile(a, d, b))
			} else {
				this.sendMessage({
							method : "chooseFile",
							params : [a, d, b],
							event : this.registerCallback(c)
						})
			}
		},
		showPageSetupDialog : function(a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.showPageSetupDialog())
			} else {
				this.sendMessage({
							method : "showPageSetupDialog",
							event : this.registerCallback(a)
						})
			}
		},
		getLastSettings : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getLastSettings(b))
			} else {
				this.sendMessage({
							method : "getLastSettings",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		getAbsoluteURL : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.getAbsoluteURL(b, a))
			} else {
				this.sendMessage({
							method : "getAbsoluteURL",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		setLastSettings : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.setLastSettings(b, a))
			} else {
				this.sendMessage({
							method : "setLastSettings",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		setDefaultPrinter : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.setDefaultPrinter(b))
			} else {
				this.sendMessage({
							method : "setDefaultPrinter",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		openFile : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.openFile(b))
			} else {
				this.sendMessage({
							method : "openFile",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		getPrinterInfo : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.getPrinterInfo(b))
			} else {
				this.sendMessage({
							method : "getPrinterInfo",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		},
		getPrinterStatus : function(b, a, c) {
			if (this.proxy) {
				(c || this.nothing).call(this, this.proxy.getPrinterStatus(b, a))
			} else {
				this.sendMessage({
							method : "getPrinterStatus",
							params : [b, a],
							event : this.registerCallback(c)
						})
			}
		},
		setCodeImage : function(x, k) {
			if (this.proxy) {
				return
			}
			var b = null;
			if (!x) {
				var a = document.querySelectorAll("embed[type$=jatoolsCoder]");
				if (a && a.length) {
					var c = a[0];
					var i = c.getAttribute("_code");
					var f = c.getAttribute("_codestyle");
					var j = document.createElement("img");
					j.setAttribute("_code", i);
					j.setAttribute("_codestyle", f);
					j.setAttribute("style", c.getAttribute("style"));
					var g = window.getComputedStyle(c, null);
					j.style.width = g.getPropertyValue("width");
					j.style.height = g.getPropertyValue("height");
					c.parentNode.replaceChild(j, c)
				}
				b = (k || document).querySelectorAll("img[_code]")
			} else {
				b = [x]
			}
			if (b.length) {
				var h = [];
				for (var d = 0; d < b.length; d++) {
					var e = b[d];
					h.push({
								width : e.offsetWidth,
								height : e.offsetHeight,
								code : e.getAttribute("_code"),
								codestyle : e.getAttribute("_codestyle")
							})
				}
				this.sendMessage({
							method : "getCodeData",
							params : [h],
							event : this.registerCallback(function(z) {
										for (var w = 0; w < z.length; w++) {
											b[w].style.width = "";
											b[w].style.height = "";
											b[w].src = z[w]
										}
									})
						})
			}
		},
		nothing : function() {
		},
		setPrintBackground : function(b, a) {
			if (this.proxy) {
				(a || this.nothing).call(this, this.proxy.setPrintBackground(b))
			} else {
				this.sendMessage({
							method : "setPrintBackground",
							params : [b],
							event : this.registerCallback(a)
						})
			}
		}
	}).initialize()
}
var _jp = null;
function getJatoolsPrinter(c) {
	alert("getPrinter..");
	if (!_jp) {
		var c = c || document;
		var d = navigator.userAgent.match(/(msie\s|trident.*rv:)([\w.]+)/i) ? c.getElementById("ojatoolsPrinter") : c.getElementById("ejatoolsPrinter");
		_jp = new JP(d)
	}
	return _jp
}
function isChrome45() {
	var c = navigator.userAgent.match(/Chrom(e|ium)\/([0-9]+)\./);
	if (c) {
		var d = parseInt(c[2], 10);
		if (d >= 35) {
			return true
		}
	}
	return false
}
function declareJatoolsPrinter(c) {
	c = c || document;
	var f = null;
	var e = "";
	if ((Object.prototype.toString.call(c) === "[object String]")) {
		e = ' license="' + c + "' ";
		f = document
	} else {
		f = c
	}
	var d = '<object id="ojatoolsPrinter" ' + e + 'codebase="jatoolsPrinter.cab#version=5,4,0,0" classid="clsid:B43D3361-D075-4BE2-87FE-057188254255" width="0" height="0">';
	if (!isChrome45()) {
		d += '<embed id="ejatoolsPrinter" type="application/x-vnd.jatoolsPrinter" width="0" height="0" />'
	}
	d += "</object>";
	f.writeln(d)
}
isChrome45() && window.addEventListener("load", function() {
			_jp = new JP();
			alert("么么哒");
			_jp.isInstalled(function(b) {
				alert("isinstalled...");
						if (!b) {
							alert("未安装！")
						} else {
							_jp.setCodeImage()
						}	 
				_jp.setCodeImage();
					})
		}, false);