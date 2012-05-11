$(document).ready(function() {

  // Init timepicker
  $(".dropdown-timepicker").click(function() {
    $(this).timepicker({
      defaultTime: 'current',
      minuteStep: 15,
      disableFocus: true,
      template: 'dropdown'
    });
  });

  // Init date picker
  var today = new Date();
  var date = "0" + (today.getMonth() + 1) + "-" + today.getDate() + "-" + today.getFullYear();
  $(".input-append.date").attr("data-date", date);

  $(".input-append.date").datepicker()
  .on('changeDate', function(e) {
    $(".input-append.date").datepicker("hide");
  });

})