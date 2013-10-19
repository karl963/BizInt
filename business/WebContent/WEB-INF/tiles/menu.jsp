

<div class=menu_position>
	<div class=menu_button_text>
		<a href="vaadeProjektid.htm">Projektid</a>
	<div class=pipeline_text>
		<a href="vaadePipeline.htm">Pipeline</a>
	<div class=tootajad_text>
		<a href="vaadeTootajadTabel.htm">T&ouml;&ouml;tajad</a>
	<div class=rahavoog_text>
		<a href="vaadeRahavoog.htm">Rahavoog</a>
	</div>
	</div>
	</div>
	</div>
</div>

<div class=login_as> 
	Logitud kui: <b><%= session.getAttribute("kasutajaNimi")%></b> ,
	<button type="button" class="projektDetailNupp" onclick="javascript:document.location.href='vaadeProjektid.htm?logivalja=1'">logi välja</button>
</div>