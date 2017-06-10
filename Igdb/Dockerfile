FROM microsoft/dotnet:1.1-sdk
WORKDIR /app

# copy csproj and restore as distinct layers
COPY Igdb.csproj ./
RUN dotnet restore

# copy and build everything else
COPY . ./
RUN dotnet publish -c Release -o out
ENTRYPOINT ["dotnet", "out/Igdb.dll"]