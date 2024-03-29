
(function () {

  var width, height, canvas, ctx, points, target, animateHeader = true;
  var minConnectionsPerNode = 7; // was 5 before
  var numberOfPoints = 200;
  var closestNeighbourDistance = 40000; // was 4000
  var closeNeighbourDistance = 80000; // was 20000
  var farNeighbourDistance = 110000; // was 40000
  var radiusConstant = 6;
  var listenForMouse = false;

  // Main
  initCircles();
  initAnimation();
  initStartingAnim();
  addListeners();

  function initCircles() {
    width = window.innerWidth < 1920 ? window.innerWidth : 1920;
    height = window.innerHeight < 1080 ? window.innerHeight : 1080;
    numberOfPoints = (200 / 1920) * width;
    target = {x: -1, y: -1};

    canvas = document.getElementById('demo-canvas');
    canvas.width = width;
    canvas.height = height;
    ctx = canvas.getContext('2d');

    // create points
    points = [];
    for (var x = 0; x < canvas.width; x = x + canvas.width / Math.sqrt(numberOfPoints)) {
      for (var y = 0; y < canvas.height; y = y + canvas.height / Math.sqrt(numberOfPoints)) {
        var px = x + Math.random() * 80;
        var py = y + Math.random() * 80;
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

  // Event handling
  function addListeners() {
    if (!('ontouchstart' in window)) {
      window.addEventListener('mousemove', mouseMove);
    }
    window.addEventListener('scroll', scrollCheck);
    window.addEventListener('resize', resize);
  }


  function mouseMove(e) {

    if (listenForMouse === false)
      return;
    target.x = width / 2;
    target.y = height / 2;
  }

  function scrollCheck() {
    if (document.body.scrollTop > height) {
      animateHeader = false;
    } else {
      animateHeader = true;
    }
  }

  function resize() {
    width = window.innerWidth < 1920 ? window.innerWidth : 1920;
    height = window.innerHeight < 1080 ? window.innerHeight : 1080;
    closestNeighbourDistance  = (closestNeighbourDistance / 1080) * width;
    closeNeighbourDistance = (closeNeighbourDistance / 1080) * width;
    farNeighbourDistance = (farNeighbourDistance / 1080) * width;
    canvas.width = width;
    canvas.height = height;
    initCircles();
    initAnimation();
  }

  // animation
  function initAnimation() {
    requestAnimationFrame(animate);
    for (var i in points) {
      shiftPoint(points[i]);
    }
  }

  function updateX(newX) {
    setTimeout(function () {
      target.x = newX;
    }, 10);
  }



  async function initStartingAnim() {

    var i = 0;
    const delay = ms => new Promise(res => setTimeout(res, ms));



    var constHeight1 = height / 4;
    var constHeight2 = 3 / 4 * height;
    await delay(300);

    while (i < width + 400) {
      await delay(80);
      target.y = constHeight1;
      target.x = i;
      i += 50;
      await delay(80);
      target.y = constHeight2;
    }
    await delay(300);
    target.x = width / 2;
    target.y = height / 2;
    for(var i = 0; i < 3; i++) {
      await delay(1000);
      closestNeighbourDistance *= 1.15; // was 4000
      closeNeighbourDistance *= 1.15; // was 20000
      farNeighbourDistance *= 1.15; // was 40000
    }
  }

  function animate() {
    if (animateHeader) {
      ctx.clearRect(0, 0, width, height);
      for (var i in points) {
        // detect points in range

        if (Math.abs(getDistance(target, points[i])) < closestNeighbourDistance) {

          points[i].active = 0.5;
          points[i].circle.active = 1;

        } else if (Math.abs(getDistance(target, points[i])) < closeNeighbourDistance) {

          points[i].active = 0.25;
          points[i].circle.active = 0.5;
        } else if (Math.abs(getDistance(target, points[i])) < farNeighbourDistance) {

          points[i].active = 0.04;
          points[i].circle.active = 0.15;
        } else {
          points[i].active = points[i].active > 0.01 ? 0.997 * points[i].active : 0;
          points[i].circle.active = points[i].circle.active > 0.02 ? 0.997 * points[i].circle.active : 0;
        }


        drawLines(points[i]);
        points[i].circle.draw();
      }
    }
    requestAnimationFrame(animate);
  }

  function shiftPoint(p) {
    TweenLite.to(p, 1 + Math.random(), {
      x: p.originX - 50 + Math.random() * 100,
      y: p.originY - 50 + Math.random() * 100, ease: Circ.easeInOut,
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


      var middleXOfLine = (p.x + p.closest[i].x) / 2;
      var min = Math.min(p.active,p.closest[i].active);
      ctx.lineWidth = 1.1;
      if (middleXOfLine < canvas.width / 2) {
        ctx.strokeStyle = 'rgba(255, 255, 255,' +  min + ')';
      } else {
        ctx.strokeStyle = 'rgba(0, 0, 0,' + min + ')';
      }

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
      if (pos.x < canvas.width / 2) {
        ctx.fillStyle = 'rgba(255, 255, 255,' + _this.active + ')';
      } else {
        ctx.fillStyle = 'rgba(0, 0, 0,' + _this.active + ')';
      }

      ctx.fill();
    };
  }

  // Util
  function getDistance(p1, p2) {
    return  p1.x === -1 ? 1000000000 : Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
  }

})();
