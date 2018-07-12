#version 330 core

uniform sampler2D colorMap;
in vec4 frontColor;
in vec2 vtexCoord;

out vec4 fragColor;

uniform float time;
uniform float speed=.1;



void main()
{
	vec2 avtexCoord = vec2(
		vtexCoord.x + time * speed,
 		vtexCoord.y + time * speed
	);
    fragColor = frontColor * texture(colorMap, avtexCoord);
}
