#version 330 core

in vec4 frontColor;
in vec2 vtexCoord;

out vec4 fragColor;

void main()
{
  float dist = distance(vtexCoord, vec2(.5,.5));
  float st = step(.2, dist);
  gl_FragColor = vec4(1, st, st, 1);

}
