#version 330 core

in vec4 frontColor;
out vec4 fragColor;

uniform int n;

void main()
{
  if(n > 0){
    if((int(gl_FragCoord.y - .5) % n) == 0)
        discard;
      }
  fragColor = frontColor;
}
