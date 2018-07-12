#version 330 core

in vec4 frontColor;
in float xNdc;
out vec4 fragColor;

uniform float time;

void main()
{
  if(step(time - 1, xNdc) == 1)
    discard;
  else
    fragColor = vec4(0,0,1,1);
}
