var page = require('webpage').create();
page.open('http://healthmap.org/ln.php?4246488&promed&0', function(status) {
  console.log("Status: " + status);
  if(status === "success") {
    page.render('example.png');
  }
  phantom.exit();
});