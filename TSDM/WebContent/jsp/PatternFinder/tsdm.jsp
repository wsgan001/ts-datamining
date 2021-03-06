	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="org.ck.gui.Constants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Temporal Pattern Detection</title>
</head>
<body>
	Temporal Pattern Detection

	<div>
		<label>Select Data Set</label>
		<select id="dropdown" class="TopMenuButtons">
			<%
				Constants.DatasetOptions[] datasets = Constants.DatasetOptions.values();
				for(int i=0; i<datasets.length; i++)
					out.println("<option value=\"" + datasets[i] + "\">" + datasets[i] + "</option>");
			%>
		</select>
		<br/>
		<br/>
		<label for="fitness">Fitness Value : </label>
		<input type="text" id="fitness" style="border: 0; font-weight: bold; "  readonly maxlength="10" size="7"/>
		 <div id="slider" style="margin-top: 1%; margin-left: auto; margin-right: auto;"></div>
		 <br/>		
		<input id="button_tsdmCalc" class="TopMenuButtons" style="" type="button" value="Run Engine"/>		
	</div>
		
	<p id="ajax_tsdm_results">		
	</p>
	
	<script type="text/javascript">
  			$(function() {
   				 $( "#slider" ).slider({
					range: "min",
					min: 0,
					max: 10,
					value: 5,
					step: 0.000005,
					slide: function ( event, ui ) {
				        $( "#fitness" ).val( ui.value );				        
				      }
   				 });
   				$( "#fitness" ).val( $( "#slider" ).slider( "value" ) );
			  });
  </script>
	
	<script type="text/javascript">	
		$("#button_tsdmCalc").button().click(function () {			
			$("#navigator").empty();							//BUG FIX 
			$("#ajax_tsdm_results").html(loadingImgHTML);			
	        $("#ajax_tsdm_results").load("MainController", 
	        		{"taskType" : "<%=Constants.TaskType.TEMPORAL_PATTERN_MINER %>",
	        		 "algorithmType" : "<%=Constants.AlgorithmType.TSDM %>",
	        		 "dataset" : $("#dropdown option:selected").val(),
	        		 "params" : $("#fitness").val()
	        		}
	        );
	        $("#ui-tabs-4").css("margin-bottom", "100%");
	    });
	</script>
	
	
</body>
</html>