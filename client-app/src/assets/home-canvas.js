
(function () {

  var width, height, canvas, ctx, points, target, animateHeader = true;
  var minConnectionsPerNode = 10; // was 5 before
  var numberOfPoints = 200;
  var closestNeighbourDistance = 8000; // was 4000
  var closeNeighbourDistance = 24000; // was 20000
  var farNeighbourDistance = 60000; // was 40000
  var radiusConstant = 6;
  const delay = ms => new Promise(res => setTimeout(res, ms));

  // Main
  initCircles();
  initAnimation();
  initPos();

  function initCircles() {
    width = window.innerWidth < 1920 ? window.innerWidth/2.5 : 1920/2.5;
    height = window.innerHeight < 1080 ? window.innerHeight/1.6 : 1080/1.6;
    target = {x: -1, y: -1};

    canvas = document.getElementById('demo-canvas');
    canvas.width = width;
    canvas.height = height;
    ctx = canvas.getContext('2d');

    // create points
    points = [];
    for (var x = 0; x < canvas.width; x = x + canvas.width / Math.sqrt(numberOfPoints)) {
      for (var y = 0; y < canvas.height; y = y + canvas.height / Math.sqrt(numberOfPoints)) {
        var px = x + 80;
        var py = y + 80;
        var p = {x: px, originX: px, y: py, originY: py};
        points.push(p);
      }
    }

    // for each point find the 5 closest points
    for (var i = 0; i < points.length; i++) {
      var closest = [];
      var p1 = points[i];
      for (var j = 0; j < points.length; j++) {
        var p2 = points[j]
        if (!(p1 == p2)) {
          var placed = false;
          for (var k = 0; k < minConnectionsPerNode; k++) {
            if (!placed) {
              if (closest[k] == undefined) {
                closest[k] = p2;
                placed = true;
              }
            }
          }

          for (var k = 0; k < minConnectionsPerNode; k++) {
            if (!placed) {
              if (getDistance(p1, p2) < getDistance(p1, closest[k])) {
                closest[k] = p2;
                placed = true;
              }
            }
          }
        }
      }
      p1.closest = closest;
    }

    // assign a circle to each point
    for (var i in points) {
      var c = new Circle(points[i], radiusConstant + Math.random() * 2, 'rgba(0, 0, 0, 0.5)');
      points[i].circle = c;
    }
  }


  // animation
  async function initAnimation() {
    animate();
    await delay(500);
    for (var i in points) {
      shiftPoint(points[i]);
    }
  }

  function updateX(newX) {
    setTimeout(function () {
      target.x = newX;
    }, 10);
  }

  async function initPos() {
    target.x = width / 2 - 30;
    target.y = height / 2 - 30;
  }

  function animate() {
    if (animateHeader) {
      ctx.clearRect(0, 0, width, height);
      for (var i in points) {
        // detect points in range

        if (Math.abs(getDistance(target, points[i])) < closestNeighbourDistance) {

          points[i].active = 0.4;
          points[i].circle.active = 0.8;

        } else if (Math.abs(getDistance(target, points[i])) < closeNeighbourDistance) {

          points[i].active = 0.2;
          points[i].circle.active = 0.5;
        } else if (Math.abs(getDistance(target, points[i])) < farNeighbourDistance) {

          points[i].active = 0.05;
          points[i].circle.active = 0.2;
        } else {
          points[i].active = points[i].active > 0.005 ? 0.997 * points[i].active : 0;
          points[i].circle.active = points[i].circle.active > 0.01 ? 0.997 * points[i].circle.active : 0;
        }


        drawLines(points[i]);
        points[i].circle.draw();
      }
    }
    requestAnimationFrame(animate);
  }

  function shiftPoint(p) {
    TweenLite.to(p, 4 + Math.random(), {
      x: p.originX + (Math.random() * 20 - 10),
      y: p.originY + (Math.random() * 20 - 10), ease: Circ.easeInOut,
      onComplete: function () {
        shiftPoint(p);
      }
    });
  }

  // Canvas manipulation
  function drawLines(p) {
    if (!p.active) return;
    for (var i in p.closest) {
      ctx.beginPath();
      ctx.moveTo(p.x, p.y);
      ctx.lineTo(p.closest[i].x, p.closest[i].y);
      ctx.strokeStyle = 'rgba(0, 0, 0,' + p.active / 2 + ')';
      ctx.lineWidth = 2;
      ctx.stroke();
    }
  }

  function Circle(pos, rad, color) {
    var _this = this;

    // constructor
    (function () {
      _this.pos = pos || null;
      _this.radius = rad || null;
      _this.color = color || null;
    })();

    this.draw = function () {
      if (!_this.active) return;
      ctx.beginPath();
      ctx.arc(_this.pos.x, _this.pos.y, _this.radius, 0, 2 * Math.PI, false);
      ctx.fillStyle = 'rgba(0, 0, 0,' + _this.active + ')';
      ctx.fill();
    };
  }

  // Util
  function getDistance(p1, p2) {
    return  p1.x === -1 ? 1000000000 : Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
  }

})();
