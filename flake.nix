{
    description = "Skills Telemetry API";

    inputs = {
        nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
        flake-utils.url = "github:numtide/flake-utils";
    };

    outputs = { self, nixpkgs, flake-utils, ... }: flake-utils.lib.eachDefaultSystem( system: 
        let
            javaOverlay = final: prev: {
                jdk = prev.temurin-bin-25;
                jre = prev.temurin-jre-bin-25;
            };

            pkgs = import nixpkgs {
                inherit system;
                overlays = [ javaOverlay ];
            };
        in {
            devShells = {
                default = pkgs.mkShell {
                    buildInputs = [
                        pkgs.jdk
                        pkgs.sbt
                        pkgs.sqlite
                    ];
                    shellHook = ''
                        export JAVA_HOME=${pkgs.jdk}
                    '';
                };
            };
        }
    );
}