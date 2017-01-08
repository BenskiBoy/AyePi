<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>IoT Lab Status</title>

  <link rel="stylesheet" href="css/normalize.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==" crossorigin="anonymous">
  <link rel="stylesheet" href="css/styles.css">

  <!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>

<body>
  <div class="container-fluid">
    <div class="row">
      <div class="room-status col-xs-12 col-md-4 col-md-offset-4">
          <h1 class="text-center">IoT Lab Status</h1><br>
          <span id="stat_icon" class="text-center glyphicon" aria-hidden="true"></span>
          <h3 class="text-center">Room is <span id="stat"><span></h3>
      </div>
    </div>
    <div class="row">
      <div class="card-outer col-xs-12">
        <div class="card">
          <h4>Door is unlocked</h4>
        </div>
      </div>
    </div>
  </div>

  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>

  <script type="text/javascript">
    $.ajax({
      url: 'main.php',
      method: 'GET',
      success: function(response) {
        var res = $.trim(response);
        if(res == '1') {
          $(".room-status").removeClass("room-active").addClass("room-inactive");
          $("#stat").html("inactive!");
          $("#stat_icon").addClass("glyphicon-remove-circle").removeClass("glyphicon-ok-circle");
        }
        else{
          $(".room-status").addClass("room-active").removeClass("room-inactive");
          $("#stat").html("active!");
          $("#stat_icon").addClass("glyphicon-ok-circle").removeClass("glyphicon-remove-circle");
        } 
      }  
      });
  </script>
</body>
</html>