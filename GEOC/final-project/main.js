points = [];

function convexHull(points) {
  // We sort the points by x coordinate.
  points.sort(function (a, b) {
    return a.x != b.x ? a.x - b.x : a.y - b.y;
  });

  var n = points.length;
  var hull = [];

  for (var i = 0; i < 2 * n; i++) {

    var j;

    // We make two iterations over the points, from 0 to n
    // and from n to 0, first to build the upper chain and then
    // for building the lower chain
    if(i < n) {
      j = i;
    }
    else {
      j = 2 * n - 1 - i;
    }
      
    // We pop points that make adding points[j] a left turn
    while (
      hull.length >= 2 &&
      removeMiddle(
        hull[hull.length - 2],
        hull[hull.length - 1],
        points[j]
      ))
    {
      hull.pop();
    }
    hull.push(points[j]);
  }

  // For not repeating point 0
  hull.pop();
  return hull;
}

function cross(a, b, c)
{
  return (a.x - b.x) * (c.y - b.y) - (a.y - b.y) * (c.x - b.x);
}

function dot(a, b, c)
{
  return (a.x - b.x) * (c.x - b.x) + (a.y - b.y) * (c.y - b.y);
}

function removeMiddle(a, b, c) {
  // This function returns > 0 if there is need to remove the b
  // point from the convex hull

  // cross is used to determine wether if we have to do a
  // right or a left turn when going from ab to c.

  var cval = cross(a, b, c);

  // dot returns the magnitude of the projection of b
  // over a and c. If this magnitude is negative or 0, we delete the
  // point if it lies on the edge from a to c.
  
  var dval = dot(a, b, c);

  // We then return true if we have to perform a left turn to add c to our
  // polygon. If the point is aligned and we have no "turn" to make, we
  // return wether if point b is the same as c or not. 
  return cval < 0 || cval == 0 && dval <= 0;
}

// =======================================================================
// DRAWING STUFF
// =======================================================================
var c = document.getElementById("myCanvas");
var ctx = c.getContext("2d");

pointRadius = 5;

function buildConvexHull()
{
  ctx.fillStyle = 'blue';

  drawPoints(points);

  hull = convexHull(points);

  ctx.fillStyle = 'red';
  ctx.strokeStyle = 'red';

  drawPoints(hull);
  drawPolygon(hull);
}


function drawPolygon(points)
{
  var next;
  for (i = 0; i < points.length - 1; i++) {
    var current = points[i];
    next = points[i + 1];
    ctx.moveTo(current.x, current.y);
    ctx.lineTo(next.x, next.y);
    ctx.stroke(); 
  } 
  var first = points[0];
  ctx.moveTo(next.x, next.y);
  ctx.lineTo(first.x, first.y);
  ctx.stroke(); 
}

function drawPoints(points)
{
  points.forEach(function(p){
    ctx.fillRect(
      p.x - pointRadius, p.y - pointRadius,
      pointRadius * 2, pointRadius * 2);
  });
}

function addPoint(evt)
{
  pos = getMousePos(c, evt);
  points.push(pos);
  ctx.clearRect(0, 0, c.width, c.height);
  ctx.beginPath();
  ctx.fillStyle = 'black';
  drawPoints(points);
}

function getMousePos(canvas, evt) {
  var rect = canvas.getBoundingClientRect();
  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}

function reset()
{
  ctx.clearRect(0, 0, c.width, c.height);
  ctx.beginPath();
  points = [];
}
