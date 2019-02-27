(function () {

  var width, height, canvas, ctx, points, target, animateHeader = true;
  var minConnectionsPerNode = 7; // was 5 before
  var numberOfPoints = 200;
  var closestNeighbourDistance = 30000; // was 4000
  var closeNeighbourDistance = 60000; // was 20000
  var farNeighbourDistance = 80000; // was 40000
  var degradationRate = 0.4;
  var listenForMouse = false;

  // Main
  initCircles();
  initAnimation();
  initStartingAnim();
  addListeners();

  function initCircles() {
    width = window.innerWidth;
    height = window.innerHeight;
    target = {x: 0, y: 0};


    canvas = document.getElementById('demo-canvas');
    canvas.width = width;
    canvas.height = height;
    ctx = canvas.getContext('2d');

    // create points
    points = [];
    for (var x = 0; x < width; x = x + width / Math.sqrt(numberOfPoints)) {
      for (var y = 0; y < height; y = y + height / Math.sqrt(numberOfPoints)) {
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
      var c = new Circle(points[i], 4 + Math.random() * 2, 'rgba(0, 0, 0, 0.5)');
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

    if(listenForMouse === false)
      return;

    var posx = posy = 0;
    if (e.pageX || e.pageY) {
      posx = e.pageX;
      posy = e.pageY;
    } else if (e.clientX || e.clientY) {
      posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
      posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
    }
    target.x = posx;
    target.y = posy;
  }

  function scrollCheck() {
    if (document.body.scrollTop > height) {
      animateHeader = false;
    } else {
      animateHeader = true;
    }
  }

  function resize() {
    width = window.innerWidth;
    height = window.innerHeight;
    canvas.width = width;
    canvas.height = height;
  }

  // animation
  function initAnimation() {
    animate();
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
    var constHeight2 = 3/4 * height;
    while (i < width + 400) {
      await delay(50);
      target.y = constHeight1;
      target.x = i;
      i += 50;
      await delay(50);
      target.y = constHeight2;
    }


    listenForMouse = true;
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
          points[i].circle.active = 0.4;
        } else if (Math.abs(getDistance(target, points[i])) < farNeighbourDistance) {

          points[i].active = 0.05;
          points[i].circle.active = 0.1;
        } else {
          points[i].active = points[i].active > 0.005 ? 0.995 * points[i].active : 0;
          points[i].circle.active = points[i].circle.active > 0.01 ? 0.995 * points[i].circle.active : 0;
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
      if (middleXOfLine < window.innerWidth / 2) {
        ctx.strokeStyle = 'rgba(255, 255, 255,' + p.active + ')';
      } else {
        ctx.strokeStyle = 'rgba(0, 0, 0,' + p.active + ')';
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
      if (pos.x < window.innerWidth / 2) {
        ctx.fillStyle = 'rgba(255, 255, 255,' + _this.active + ')';
      } else {
        ctx.fillStyle = 'rgba(0, 0, 0,' + _this.active + ')';
      }

      ctx.fill();
    };
  }

  // Util
  function getDistance(p1, p2) {
    return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
  }

})();
