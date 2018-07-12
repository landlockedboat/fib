#version 330 core

in vec4 frontColor;
out vec4 fragColor;

void main()
{
  vec4 fragCoord = gl_FragCoord;
  float N = dFdx(fragCoord.x);

  fragColor = frontColor * N;
}
