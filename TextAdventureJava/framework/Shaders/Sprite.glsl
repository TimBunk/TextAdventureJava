!VERTEX
#version 330

layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec2 aTextureCoordinate;
layout (location = 2) in vec4 aColor;
layout (location = 3) in mat4 model;

out vec2 textureCoordinate;
out vec4 color;

uniform mat4 projection;

void main() {
  gl_Position = projection * model * vec4(aPosition, 0.0f, 1.0f);
  textureCoordinate = aTextureCoordinate;
  color = aColor;
}
!END

!FRAGMENT
#version 330

out vec4 fragColor;

in vec2 textureCoordinate;
in vec4 color;

uniform sampler2D ourTexture;

void main() {

  fragColor = texture(ourTexture, textureCoordinate);
  fragColor = vec4(fragColor.r + color.r, fragColor.g + color.g, fragColor.b + color.b, fragColor.a * color.a);

}
!END
