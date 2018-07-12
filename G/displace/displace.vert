#version 330 core

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in vec2 texCoord;

out vec4 frontColor;
out vec2 vtexCoord;

uniform sampler2D heightMap;
uniform float scale = 0.05;

uniform mat4 modelViewProjectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat3 normalMatrix;

out vec3 eye;

void main()
{
	vec2 st = 0.49 * vertex.xy + vec2(0.5);
	
	float r = texture(heightMap, st).r;
	
	vec3 pos = vertex + (normal * r * scale);
	eye = (modelViewMatrix * vec4(pos, 1.0)).xyz;
	
    vec3 N = normalize(normalMatrix * normal);
    //frontColor = vec4(color,1.0) * N.z;
    vtexCoord = texCoord;
    gl_Position = modelViewProjectionMatrix * vec4(pos, 1.0);
}
