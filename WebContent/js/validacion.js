/**
 * 
 */

function submitByEnter(e, boton) {
	if (e.keyCode == 13) {
		document.getElementById(boton).click();
		return false;
	} else {
		return true;
	}
}

function selectAll(checkbox, name) {
	var elements = checkbox.form.elements;
	for (var i = 0; i != elements.length; i++) {
		var element = elements[i];
		var patt1 = new RegExp(name + "$", "g");
		if (patt1.test(element.id)) {
			element.checked = checkbox.checked;
		}
	}
}

function selectOneAll(checkbox, name, idOneAll) {
	var elements = checkbox.form.elements;
	var ban = true;
	for (var i = 0; i != elements.length; i++) {
		var element = elements[i];
		var patt1 = new RegExp(name + "$", "g");
		if (patt1.test(element.id)) {
			if (!element.checked)
				ban = false;
		}
	}
	idOneAll.checked = ban;
}

function validarSeleccionado(btn, name) {
	var elements = "";
	try {
		elements = btn.form.elements;
	} catch (e) {
		elements = document.getElementById(btn).elements;
	}
	for (var i = 0; i != elements.length; i++) {
		var element = elements[i];
		var patt1 = new RegExp(name + "$", "g");
		if (patt1.test(element.id)) {
			if (element.checked)
				return true;
		}
	}
	return false;
}

/**
 * Este metodo permite ejecutar el boton con id upload, el cual permite cargar
 * un archivo.
 * 
 * @modify 27-05-2014 marisol.calderon
 * 
 * @param idForm:
 *            id del formulario donde se ejecuta la carga del archivo.
 * @param ancla:
 *            id del panel donde se quiere anclar luego de cargar el archivo.
 */
function controladorUpload(idForm, ancla) {
	var valAncla = idForm + ':' + ancla;
	var idAncla = document.getElementById(valAncla);
	if (idAncla != undefined && idAncla != '') {
		anyadirAncla(idForm, valAncla);
	}
	document.getElementById(idForm + ':upload').click();
}

/**
 * Este metodo permite configurar dinamicamente un campo tipo fileUpload,
 * modificando los valores del onchange segun sea necesario, para validar el
 * tamanyo del archivo antes de ser cargado.
 * 
 * @modify 04-06-2014 marisol.calderon
 * 
 * @param idForm:
 *            id del formulario desde donde se realiza la carga del archivo.
 * @param ancla:
 *            id de parte del formulario donde se quiere anclar al realizar la
 *            carga del archivo.
 * @param maxSize:
 *            tamanyo maximo del archivo a cargar.
 */
function configUpload(idForm, ancla, maxSize) {
	var uploadFile = document.getElementById(idForm + ':uploadFile');
	uploadFile.removeAttribute("onchange");
	if (verificIE()) {
		uploadFile.setAttribute("onchange", "checkFileSize(this, '" + maxSize
				+ "' , '" + idForm + "', '" + ancla + "');");
	} else {
		uploadFile['onchange'] = new Function("checkFileSize(this, '" + maxSize
				+ "', '" + idForm + "', '" + ancla + "');");
	}
	uploadFile.click();
}

/**
 * Este metodo ancla el formulario en una parte especifica cuando se realiza un
 * submit.
 * 
 * @param idForm:
 *            id del formulario donde se quiere realizar el ancla.
 * @param ancla:
 *            id de parte del formulario donde se quiere anclar.
 */
function anyadirAncla(idForm, ancla) {
	document.getElementById(idForm).action = document.getElementById(idForm).action
			+ '#' + ancla;
}

function validaDouble(inpuText, decimales) {
	if (decimales > 0) {
		var patron = new RegExp('^\\\d+(\\\.\\\d{1,' + decimales + '})?$');
		if (inpuText.value == "") {
			inpuText.value = "0.0";
			return true;
		} else if (!patron.test(inpuText.value)) {
			inpuText.value = "0.0";
			return false;
		} else {
			inpuText.value = parseFloat(inpuText.value);
			return true;
		}
	} else {
		alert('no se puede validar un Double si no especifica un valor mayor a cero para los decimales');
	}
}

// Verifica el navegador
function verificIE() {
	var gko = navigator.userAgent.toLowerCase();
	if (gko.indexOf('gecko') != -1) {
		return true;
	} else {
		return false;
	}
}

/**
 * Establecer el foco en el elemento del id enviado.
 * 
 * @param id
 *            El id del elemento para establecer el foco.
 */
function setFocus(id) {
	var element = document.getElementById(id);
	var idHidden;
	if (element && element.focus) {
		element.focus();
		var idConFoco = document.activeElement.id;
		if (idConFoco != id) {
			if (element.type == 'hidden') {
				idHidden = id + '2';
				if (document.getElementById(idHidden) != undefined) {
					id = idHidden;
				}
			}
			document.getElementById(id).scrollIntoView(true);
		}
	}
}

/**
 * Metodo que permite seleccionar un solo radio en el extendDataTable.
 * 
 * @author marisol.calderon
 * 
 * @param radio:
 *            radio que se quiere seleccionar.
 */
function extendDataTableSelectOneRadio(radio) {
	var id = radio.name.substring(radio.name.lastIndexOf(':'));
	var el = radio.form.elements;
	for (var i = 0; i != el.length; i++) {
		if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) {
			el[i].checked = false;
		}
	}
	radio.checked = true;
}

/**
 * This method allows execute a button.
 * 
 * @author marisol.calderon
 * 
 * @param idButton:
 *            id of the button to execute.
 */
function clickButton(idButton) {
	document.getElementById(idButton).click();
}

/**
 * Este metodo valida el tamanyo maximo de los archivos, enviando el valor en
 * MB.
 * 
 * @author marisol.calderon
 * 
 * @param inputFile:
 *            objeto del inputFile a cargar.
 * @param maxSize:
 *            tamanyo maximo del archivo en MB.
 * @param idForm:
 *            id del formulario desde donde se carga el archivo.
 * @param ancla:
 *            id del panel donde se quiere anclar luego de cargar el archivo.
 */
function checkFileSize(inputFile, maxSize, idForm, ancla) {
	var max = maxSize * 1024 * 1024;
	if (inputFile.files) {
		if (inputFile.files[0].size > max) {
			inputFile.value = null;
			clickButton('formGenerico:buttonValidateSizeFile');
		} else {
			controladorUpload(idForm, ancla);
		}
	}
}

/**
 * Este metodo permite asignar el valor a un campo.
 * 
 * @author marisol.calderon
 * 
 * @param id:
 *            id del campos a asignar el valor.
 * @param value:
 *            valor que se quiere asignar al campos.
 */
function setValueField(id, value) {
	document.getElementById(id).value = value;
}

/**
 * Habilita siempre la opcion de desplazarse por los formularios, por medio de
 * la tecla 'Tab' del teclado
 */
$(function() {
	var tabindex = 1;
	$('form').find('input, select, textarea, button, radio, checkbox').each(
			function() {
				if (this.type != "hidden") {
					var $input = $(this);
					$input.attr("tabindex", tabindex);
					tabindex++;
				}
			});
});

$(function() {
	var firstInput = $(
			":input:not(input[type=button],input[type=submit],button):visible:first",
			$('#contenido'));
	firstInput.focus();
});

/**
 * Establece el foco en el primer elemento de cualquier popup de busqueda a
 * traves de la clase rf-pp-cntr de los popupPanel de richfaces.
 */
function setFocusPopUp() {
	var firstElement = $(
			":input:not(input[type=button],input[type=submit],button):visible:first",
			$('.rf-pp-cntr'));
	firstElement.focus();
}

/**
 * Funcion que limpia el foco despues de hacer click en botones tipo submit.
 * 
 */
$(function() {

	$('input:submit').click(function(event) {
		$('input:submit').blur();
	});
});

/**
 * Funcion que impide envio de formulario con tecla enter.
 * 
 */
$(document).on('keyup keypress', 'form input[type="text"]', function(e) {
	if (e.which == 13) {
		e.preventDefault();
		return false;
	}
});

/**
 * This function make the scroll to initial position
 * 
 */
function scrollUp() {
	scroll(0, 0);
}

/**
 * This method validates that the entry in the inputText is only numerical.
 * 
 * @author Wilhelm.Boada
 * 
 * @param e:
 *            Event execute in keyboard..
 * @returns {Boolean}:If is true, allow the number entry, otherwise return
 *          false.
 */
function validateNumberEntry(e) {
	var key = window.Event ? e.which : e.keyCode;
	if ((key >= 48 && key <= 57) || (key == 8)) {
		return true;
	}
	return false;
}