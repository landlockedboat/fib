#version 330 core

in vec4 frontColor;
out vec4 fragColor;

uniform sampler2D bat0;
uniform sampler2D bat1;

uniform float time;

void main()
{
	vec2 tiledCoords = (gl_FragCoord.xy/64) - time;
	//this tells uf if we are between 0.5 and 1
	//bool isBat1 = mod(round(fract(time) * 100), 10) > 5;

	bool isBat1 = mod(fract(time), 0.1) > 0.05;

	vec4 C;
	if(isBat1){
		C = texture(bat1, tiledCoords);
	}
	else{
		C = texture(bat0, tiledCoords);
	}
	if(C.a > 0.2)
	    fragColor = vec4(0);
	else
		fragColor = frontColor;
}
